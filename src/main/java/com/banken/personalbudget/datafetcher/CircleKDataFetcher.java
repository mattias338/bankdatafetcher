package com.banken.personalbudget.datafetcher;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CircleKDataFetcher {
    private static final String SEARCH_TRANSACTIONS_X_PATH = "/html/body/nav/ul/li[4]/a";
    private static final String SEARCH_BUTTON_X_PATH = "//*[@id=\"searchtransbtn\"]";
    private static final String CIRCLE_K_BANK_NAME = "CircleK";

    private final WebDriver driver;
    private final List<Transaction> transactions = new ArrayList<>();
    private String owner;
    private String accountName;

    public CircleKDataFetcher(WebDriver driver, BufferedWriter logPath) {
        this.driver = driver;
    }

    public List<Transaction> fetch(String personnummer) {

        logIn(personnummer);

        goToSearchTransactions();

        getOwnerAndAccountInfo();

        search();

        return transactions;

    }

    private void getOwnerAndAccountInfo() {
        String ownerAndAccountInfoXPath = "/html/body/div[2]/main/section/div[1]/div/div/div[1]/p";
        WebElement ownerAndAccountInfo = driver.findElement(By.xpath(ownerAndAccountInfoXPath));
        String text = ownerAndAccountInfo.getText();
        String[] split = text.split(" ");
        owner = split[1] + split[0];
        accountName = split[2];
    }

    private void search() {
        // set start date. set it 3 months back, if possible
        By startDateBy = By.xpath("//*[@id=\"searchtransfrommonth\"]");
        WebElement startDate = driver.findElement(startDateBy);
        Select startDateSelect = new Select(startDate);
        List<WebElement> options = startDateSelect.getOptions();
        int numberOfMonthsToLookBack = options.size() >= 4 ? 3 : (options.size() - 1);
        startDateSelect.selectByIndex(numberOfMonthsToLookBack);

        driver.findElement(By.xpath(SEARCH_BUTTON_X_PATH)).click();


        By transactionTablesUlBy = By.xpath("//*[@id=\"transaction-tables\"]/section/ul");
        new WebDriverWait(driver, 10).
                until(ExpectedConditions.visibilityOfElementLocated(transactionTablesUlBy));

        WebElement ul = driver.findElement(transactionTablesUlBy);
        List<WebElement> entries = ul.findElements(By.className("list-item"));

        entries.forEach(entry -> parseEntry(entry));
    }

    private void parseEntry(WebElement entry) {
        WebElement element = entry;
        List<WebElement> data = element.findElements(By.tagName("li"));
        String date = data.get(0).getText();
        System.out.println("date = " + date);
        String[] split = date.split("-");
        String monthString = split[0];
        int month = Integer.parseInt(monthString);
        LocalDateTime now = LocalDateTime.now();
        int yearNow = now.getYear();
        int monthNow = now.getMonthValue();

        int year = month > monthNow ? yearNow - 1 : yearNow;

        Transaction transaction = newTransaction();
        transaction.setTransactionDate("" + year + "-" + date);

        String otherParty = data.get(2).getText();
        System.out.println("otherParty = " + otherParty);
        transaction.setOtherParty(otherParty);

        String amount = data.get(6).getText();
        System.out.println("amount = " + amount);
        transaction.setAmount("-" + amount);

        System.out.println("transaction = " + transaction);

        transactions.add(transaction);
    }

    private void logIn(String personnummer) {
        driver.get("https://id.signicat.com/std/method/seb?method=sbid-mobil&profile=cobrand&language=sv&target=https%3A%2F%2Fsecure.sebkort.com%2Fsea%2Fexternal%2FProcessSignicatResponse%3Fmethod%3Dsbid-mobil%26target%3D%252Fnis%252Fm%252Fstse%252Fexternal%252FvalidateEidLogin%26prodgroup%3D0122%26SEB_Referer%3D%252Fnis%26uname%26countryCode%3DSE");

        String mobiltBankidXPath = "/html/body/div[2]/div/div[2]/div[1]/div/div[2]/div/div/h4/a[2]";
        WebElement mobiltBankidLink = driver.findElement(By.xpath(mobiltBankidXPath));
        mobiltBankidLink.click();

        String personnummerXpath = "/html/body/div[2]/div/div[2]/div[1]/div/div[2]/div/div/div/form/span/input";
        By personnummerBy = By.xpath(personnummerXpath);
        new WebDriverWait(driver, 10).
                until(ExpectedConditions.elementToBeClickable(personnummerBy));

        WebElement personnummerInput = driver.findElement(personnummerBy);
        personnummerInput.sendKeys(personnummer);
        personnummerInput.submit();

        waitForSummary();
    }

    private void goToSearchTransactions() {
        WebElement searchTransactions = driver.findElement(By.xpath(SEARCH_TRANSACTIONS_X_PATH));
        searchTransactions.click();

        waitForSearchButton();
    }

    private void waitForSearchButton() {
        new WebDriverWait(driver, 10).
                until(ExpectedConditions.elementToBeClickable(By.xpath(SEARCH_BUTTON_X_PATH)));
    }

    private void waitForSummary() {
        new WebDriverWait(driver, 60).
                until(ExpectedConditions.elementToBeClickable(By.xpath(SEARCH_TRANSACTIONS_X_PATH)));

    }

    private Transaction newTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAccountName(accountName);
        transaction.setOwner(owner);
        transaction.setBank(CIRCLE_K_BANK_NAME);
        return transaction;
    }
}
