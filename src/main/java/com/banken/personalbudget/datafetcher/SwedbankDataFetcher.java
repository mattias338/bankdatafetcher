package com.banken.personalbudget.datafetcher;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class SwedbankDataFetcher {
//    String F = "//*[@id=\"swed-ui-id-t4opk7dzx-34\"]/ng-transclude/div/swed-page/div/div/div/ng-transclude[1]/swed-page-region[1]/div/swed-my-accounts/div/section/div/div/ul/li[1]/swed-item/div";
//    private static final String LIST_OF_ACCOUNTS_XPATH1 =                   "//*[@id=\"main\"]/ng-transclude/div/swed-page/div/div/div/ng-transclude[1]/swed-page-region[1]/div/swed-my-accounts/div/section/div/div/ul";
//    private static final String SWED_MAIN_LAYOUT = "//*[@id=\"layout\"]/div/div[2]/ui-view/ui-view/ui-view/ui-view/swed-ui-main-layout/div[2]/div[3]";
//    private static final String LIST_OF_ACCOUNTS_XPATH = "//*[@id=\"swed-ui-id-qqxvtf0sl-34\"]/ng-transclude/div/swed-page/div/div/div/ng-transclude[1]/swed-page-region[1]/div/swed-my-accounts/div/section/div/div/ul";
//    private static final String SENASTE_TRANSAKTIONER_XPATH  = "//*[@id=\"main\"]/ng-transclude/div/swed-accounts-account-history/swed-page/div/div/div/ng-transclude[1]/swed-page-region/div/div/section[2]/swed-account-table/div/table";
    private static final String SWEDBANK_NAME = "Swedbank";

    private final WebDriver driver;
    private final List<Transaction> transactions = new ArrayList<>();
    private final BufferedWriter logWriter;

    private String owner;
    private String accountName;

    private String id;

    public SwedbankDataFetcher(WebDriver driver, BufferedWriter logWriter) {
        this.driver = driver;
        this.logWriter = logWriter;
    }

    public List<Transaction> fetch(String personnummer) {
        login(personnummer);

        owner = findOwner();

        int numberOfAccounts = numberOfAccounts();
        for (int i = 0; i < numberOfAccounts; i++) {
            goToAccount(i);
            accountName = findAccountName();
            parseData();
            goToHomeScreen();
        }

//        logout();

        transactions.forEach(transaction -> transaction.setOwner(owner));
        return transactions;
    }

    private void logout() {
        String logoutButtonXPath = "//*[@id=\"sidebar\"]/swed-ui-main-layout-sidebar/swed-scrollpane/div[3]/div/div/div/swed-main-menu/ul/li[12]/swed-main-menu-item/div/button";
        WebElement logoutButton = driver.findElement(By.xpath(logoutButtonXPath));

        logoutButton.click();

        String loginButtonXPath = "//*[@id=\"loginbutton\"]";
//        new WebDriverWait(driver, 60).
//                until(ExpectedConditions.visibilityOfElementLocated(By.xpath(loginButtonXPath)));

    }

    private String findAccountName() {
        String accountNameXPath = "//*[@id=\"" + findMainId() + "\"]/ng-transclude/div/swed-accounts-account-history/swed-page/div/div/ng-transclude/sw-head/swed-hero/div/header/div[1]/ng-transclude[1]/h1";
        WebElement accountNameElement = driver.findElement(By.xpath(accountNameXPath));
        return accountNameElement.getText();
    }

    private void login(String personnummer) {
        driver.get("https://online.swedbank.se/app/privat/login");

        By userIdBy = By.name("userId");

        new WebDriverWait(driver, 10).
                until(ExpectedConditions.elementToBeClickable(userIdBy));

        WebElement userId = driver.findElement(userIdBy);

        userId.sendKeys(personnummer);
        String formXpath = "//*[@id=\"layout\"]/div/div[2]/ui-view/ui-view/swed-page/div/div[2]/div/ng-transclude[1]/swed-page-region/div/div/swed-login-form/div/div/form";
        WebElement formElement = driver.findElement(By.xpath(formXpath));
        WebElement selectElement = formElement.findElement(By.tagName("select"));
        Select select = new Select(selectElement);
        select.selectByVisibleText("Mobilt BankID eller SäkerhetsID");
        selectElement.submit();

        waitForHomeScreen();
    }

    private String findOwner() {
        String userButtonXPath = "//*[@id=\"header\"]/swed-ui-main-layout-header/swed-page-header/div[3]/div/div[2]/swed-menu-bar/ng-transclude/swed-menu[3]/button";
        WebElement userButton = driver.findElement(By.xpath(userButtonXPath));
        String owner = userButton.getText();
        return owner;
    }

    private int numberOfAccounts() {
        int size = getListOfAccounts().size();
        System.out.println("Number of accounts found " + size);
        return size;
    }

    private void goToAccount(int numberInList) {
        System.out.println("Going to account " + numberInList);
        List<WebElement> listOfAccounts = getListOfAccounts();
        WebElement firstInList = listOfAccounts.get(numberInList);
        WebElement element = firstInList.findElement(By.tagName("swed-item"));
        System.out.println("element = " + element);
        element.click();

        new WebDriverWait(driver, 10).
                until(ExpectedConditions.visibilityOfElementLocated(By.xpath(getSenasteTransaktionerXPath())));
    }

    private void goToHomeScreen() {
        String homeLinkXPath = "//*[@id=\"sidebar\"]/swed-ui-main-layout-sidebar/swed-scrollpane/div/swed-main-menu/ul/li[1]/swed-main-menu-item/div/button";
        WebElement homeLink = driver.findElement(By.xpath(homeLinkXPath));
        homeLink.click();
        waitForHomeScreen();


    }

    private void parseData() {
        WebElement senasteTransaktionerElement = driver.findElement(By.xpath(getSenasteTransaktionerXPath()));
        List<WebElement> senasteTransaktionerList = senasteTransaktionerElement.findElements(By.tagName("tbody"));

        // This list typically contain just one element
        senasteTransaktionerList.forEach(data -> parseMonth(data));


    }

    private void parseMonth(WebElement data) {
        List<WebElement> entries = data.findElements(By.tagName("tr"));
        // The first element in the list contains headers and the second element contains nothing,
        // hence, start at the third element.

        for (int i = 2; i < entries.size(); i++) {
            WebElement entry = entries.get(i);

            List<WebElement> tds = entry.findElements(By.tagName("td"));

            Transaction transaction = new Transaction();


            if (tds.size() < 1) {
                continue;
            }
            transaction.setOtherParty(getDataFromEntry(tds.get(0)));
            transaction.setTransactionDate(getDataFromEntry(tds.get(1)));
            transaction.setAmount(getDataFromEntry(tds.get(3)));
            String balance = getDataFromEntry(tds.get(4));

            transaction.setOwner(owner);
            transaction.setAccountName(accountName);
            transaction.setBank(SWEDBANK_NAME);

            System.out.println("Found data: " + transaction + " and balance " + balance + ".");
            transactions.add(transaction);
        }
    }

    private String getDataFromEntry(WebElement element) {
        WebElement data = element.findElement(By.xpath("div/div[1]"));
        String text = data.getText();
        return text;
    }

    private List<WebElement> getListOfAccounts() {
        WebElement ulAccountListElement = driver.findElement(By.xpath(getListOfAccountsXPath()));
        return ulAccountListElement.findElements(By.tagName("li"));
    }

    private void waitForHomeScreen() {
        String listOfAccountsXpath = getListOfAccountsXPath();
        new WebDriverWait(driver, 60).
                until(ExpectedConditions.visibilityOfElementLocated(By.xpath(listOfAccountsXpath)));
    }

    // In the XPath used for e.g. listOfAccounts one id is dynamic and need to be found dynamically.
    // That is the responsibility of this method.
    private String findMainId() {
        if (id == null) {
            String mainIdParent = "//*[@id=\"layout\"]/div/div[2]/ui-view/ui-view/ui-view/ui-view/swed-ui-main-layout/div[2]/div[3]";
            String childName = "main";
            String mainTag = mainIdParent + "/" + childName;
            By mainTagBy = By.xpath(mainTag);

            new WebDriverWait(driver, 60).
                    until(ExpectedConditions.visibilityOfElementLocated(By.xpath(mainIdParent)));

            WebElement mainTagElement = driver.findElement(mainTagBy);
            id = mainTagElement.getAttribute("id");
        }
        return id;
    }

    private String getListOfAccountsXPath() {
        String mainId = "//*[@id=\"" + findMainId() + "\"]/ng-transclude/div/swed-page/div/div/div/ng-transclude[1]/swed-page-region[1]/div/swed-my-accounts/div/section/div/div/ul";
        return mainId;
    }

    private String getSenasteTransaktionerXPath() {
        String senasteTransaktioner = "//*[@id=\"" + findMainId() + "\"]/ng-transclude/div/swed-accounts-account-history/swed-page/div/div/div/ng-transclude[1]/swed-page-region/div/div/section[2]/swed-account-table/div/table";
        return senasteTransaktioner;
    }
}
