package com.banken.personalbudget.datafetcher;

import com.banken.personalbudget.Common;
import com.banken.personalbudget.JsonFileStorage;
import com.banken.personalbudget.Storage;
import com.banken.personalbudget.data.Data;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    private Path dataPath;
    private BufferedWriter logWriter;

//        private static final String PERSONNUMMER = "198508015461";
    private static final String PERSONNUMMER = "198404265053";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/home/banken/development/libs/seleniumstuff/chromedriver");

        Main main = new Main();

        List<Closeable> closeables = new ArrayList<>();

        main.init(closeables);




        try {
            main.doStuff();
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

    private void doStuff() {
        List<Transaction> transactions = new ArrayList<>();
//        transactions.addAll(fetchSwedbank(driver, PERSONNUMMER));
//        transactions.addAll(fetchCircleK(driver, PERSONNUMMER));
        String directory = "/home/banken/Downloads";
        List<String> files = Arrays.asList(
//                "Transaktioner_2020-11-14_14-52-00.csv"
//                ,
//                "Transaktioner_2020-11-14_14-52-35.csv"
                "Transaktioner_2022-10-07_18-55-01.csv"
//                ,
//                "t.csv"
                );
        transactions.addAll(fetchSwedbankLocal(files.stream().map(file -> directory + "/" + file).collect(Collectors.toList()), PERSONNUMMER));


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

    List<Transaction> fetchSwedbankLocal(List<String> filename, String personnummer) {
        return new SwedbankLocalDataParser().parse(filename);
    }

    List<Transaction> fetchCircleKLocal(String year, List<String> filename, String personnummer) {
        return new CircleKLocalDataParser(year).parse(filename);
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








}
