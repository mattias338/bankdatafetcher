package com.banken.personalbudget;

import com.banken.personalbudget.data.Data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonFileStorage {

    private final Path dataPath;
    private Gson gson;

    public JsonFileStorage(Path dataPath) {
        this.dataPath = dataPath;

        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void storeData(Data data) {
        System.out.println("JsonFileStorage.storeData");
        System.out.println("data = " + data);
//        if (true) {
//            return;
//        }

        String dataJsonString = gson.toJson(data);

        try {
            Files.write(dataPath, dataJsonString.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Data getData() {
        try {
            byte[] bytes = Files.readAllBytes(dataPath);
            String dataString = new String(bytes);
            Data data = gson.fromJson(dataString, Data.class);
            return data;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        Transaction transaction1 = new Transaction();
//        transaction1.setBank("Swedbank");
//        transaction1.setAccountName("Personkonto");
//        transaction1.setOwner("Mattias");
//        transaction1.setAmount("-100.21");
//        transaction1.setOtherParty("Ica maxi");
//        transaction1.setTransactionDate("2017-10-23");
//
//        Transaction transaction2 = new Transaction();
//        transaction2.setBank("Swedbank");
//        transaction2.setAccountName("Servicekonto");
//        transaction2.setOwner("Mattias");
//        transaction2.setAmount("-5004");
//        transaction2.setOtherParty("Brf Mathildeberg");
//        transaction2.setTransactionDate("2017-11-23");
//
//        System.out.println("transaction1 = " + transaction2);
//
//
//        Data data = new Data();
//
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(transaction1);
//        transactions.add(transaction2);
//
//        data.setTransactions(transactions);
//
//        String s = gson.toJson(data);
//
//        String userHome = System.getProperty("user.home");
//
//        Path path = Paths.get(System.getProperty("user.home"), "transactions", ":data.txt");
//
//        path.getParent().toFile().mkdirs();
//
//        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)){
//            bufferedWriter.write(s);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        Data datb = gson.fromJson(s, Data.class);
//
//        System.out.println("data = " + data);
//        System.out.println("datb = " + datb);
//
//        try {
//            List<String> strings = Files.readAllLines(path);
//            String readString = strings.stream().collect(Collectors.joining("\n"));
//            assert readString.equals(s);
//
//            Data readData = gson.fromJson(readString, Data.class);
//            assert readData.equals(data);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
