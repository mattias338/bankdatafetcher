package com.banken.personalbudget.gui;

import com.banken.personalbudget.Storage;
import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FxGuiWithTestData extends FxGui {
    @Override
    protected Storage getStorage() {
        return new TestStorage();
    }


    private static class TestStorage implements Storage {
        @Override
        public void storeData(Data data) {

        }

        @Override
        public Data getData() {
            Data data = new Data();
            data.setTransactions(generateTestTransactions());
            return data;
        }

        private List<Transaction> generateTestTransactions() {
            LocalDate startDate = LocalDate.of(2019, 1, 5);
            return IntStream.range(0, 700).mapToObj(i -> {
                Transaction transaction = new Transaction();
                transaction.setAccountName("Test account 1");
                transaction.setAmount(Integer.toString(i * 10));
                transaction.setBank("Test bank 1");
                transaction.setOwner("Test owner 1");
                transaction.setOtherParty("Test receiver account " + (i % 5 + 1));
                transaction.setNewTransaction(true);
                LocalDate thisDate = startDate.plusDays(i / 2);
                transaction.setTransactionDate(thisDate.format(DateTimeFormatter.ISO_DATE));
                return transaction;
            }).collect(Collectors.toList());
        }
    }
}
