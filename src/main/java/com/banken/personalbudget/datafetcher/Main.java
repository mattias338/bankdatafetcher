package com.banken.personalbudget.datafetcher;


import com.banken.personalbudget.Common;
import com.banken.personalbudget.JsonFileStorage;
import com.banken.personalbudget.Storage;
import com.banken.personalbudget.data.Data;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    private Path dataPath;
    private BufferedWriter logWriter;

    private static final String PERSONNUMMER = "";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/home/banken/development/libs/seleniumstuff/chromedriver");

        Main main = new Main();

        List<Closeable> closeables = new ArrayList<>();

        main.init(closeables);

        WebDriver driver = new ChromeDriver();
        closeables.add(driver::close);

        // Needed by swedbank...
        try {
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } catch (WebDriverException e) {
            System.out.println("e = " + e);
        }



        try {
            main.doStuff(driver);
        } finally {
            closeables.forEach(closeable -> {
                try {
                    closeable.close();
                } catch (Exception e) {
                    PrintWriter printWriter = new PrintWriter(main.logWriter);
                    e.printStackTrace(printWriter);
                    System.out.println("Error when closing: " + e + ", see log.");
                }
            });
        }
    }

    private void doStuff(WebDriver driver) {
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(fetchSwedbank(driver, PERSONNUMMER));
//        transactions.addAll(fetchCircleK(driver, PERSONNUMMER));


        int numberOfTransactionsFound = transactions.size();
        System.out.println("numberOfTransactionsFound = " + numberOfTransactionsFound);

        Storage storage = new JsonFileStorage(dataPath);
        Data existingData = storage.getData();

        List<Transaction> newTransactions = transactions.stream().
                filter(transaction -> !isTransactionPresent(transaction, existingData)).collect(Collectors.toList());

        int numberOfNewTransactions = newTransactions.size();
        System.out.println("numberOfNewTransactions = " + numberOfNewTransactions);

        newTransactions.forEach(transaction -> transaction.setNewTransaction(true));

        existingData.getTransactions().addAll(newTransactions);

        storage.storeData(existingData);

    }

    private boolean isTransactionPresent(Transaction transaction, Data existingData) {
        boolean isPresent = existingData.getTransactions().stream().
                anyMatch(existingTransaction -> existingTransaction.sameTransactionAs(transaction));
        if (isPresent) {
            System.out.println(transaction + " is present in database.");
        }
        return isPresent;
    }

    private void init(List<Closeable> closeables) {
        dataPath = Common.getDataPath();
        Path logPath = Common.getLogPath();


        createDirectory(dataPath);
        createDirectory(logPath);

        try {
            logWriter = Files.newBufferedWriter(logPath);
            closeables.add(() -> logWriter.close());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDirectory(Path pathWithFile) {
        pathWithFile.getParent().toFile().mkdirs();
    }

    private List<Transaction> fetchSwedbank(WebDriver driver, String personnummer) {
        SwedbankDataFetcher swedbankDataFetcher = new SwedbankDataFetcher(driver, logWriter);
        List<Transaction> transactions = swedbankDataFetcher.fetch(personnummer);
        return transactions;
    }

    private List<Transaction> fetchCircleK(WebDriver driver, String personnummer) {
        CircleKDataFetcher circleKDataFetcher = new CircleKDataFetcher(driver, logWriter);
        List<Transaction> transactions = circleKDataFetcher.fetch(personnummer);
        return transactions;
    }

    private void testGoogle() {
        WebDriver driver = new ChromeDriver();

        // And now use this to visit Google
        driver.get("http://www.google.com");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Find the text input element by its name
        WebElement element = driver.findElement(By.name("q"));

        // Enter something to search for
        element.sendKeys("Cheese!");

        // Now submit the form. WebDriver will find the form for us from the element
        element.submit();

        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());

        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("cheese!");
            }
        });

        // Should see: "cheese! - Google Search"
        System.out.println("Page title is: " + driver.getTitle());

        //Close the browser
        driver.quit();

    }

    private void testBankid() {
//        WebDriver driver = new ChromeDriver();
//
//        driver.get("https://test.bankid.com/");
//
//        new WebDriverWait(driver, 4).
////                until(ExpectedConditions.visibilityOfElementLocated(By.linkText("/Mobile/OtherDevice")));
//                until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Test Mobile BankID")));
//
//
//        WebElement link = driver.findElement(By.linkText("Test Mobile BankID"));
//        link.click();
//
//        By byTestSigning = By.linkText("Test signing");
//
//        new WebDriverWait(driver, 4).
//        until(ExpectedConditions.visibilityOfElementLocated(byTestSigning));
//        driver.findElement(byTestSigning).click();
//
//        By byPersonnummer = By.id("Personnummer");
//
//        new WebDriverWait(driver, 4).
//                until(ExpectedConditions.visibilityOfElementLocated(byPersonnummer));
//        WebElement personnummberElement = driver.findElement(byPersonnummer);
//        personnummberElement.sendKeys("198404265053");
//        personnummberElement.submit();
    }
}
