package com.banken.personalbudget.gui;

import com.banken.personalbudget.Common;
import com.banken.personalbudget.datafetcher.Transaction;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class AbstractLatestResolver<T> implements LatestResolver<T> {
    private final List<Transaction> transactions;
    private final Function<Transaction, T> transactionProperty;
    private T defaulValue = null;


    public AbstractLatestResolver(List<Transaction> transactions, Function<Transaction, T> transactionProperty) {
        this.transactions = transactions;
        this.transactionProperty = transactionProperty;
    }

    public void setDefaulValue(T defaultValue) {
        this.defaulValue = defaultValue;
    }

    @Override
    public T resolve(String otherParty) {
        return transactions.stream().
                filter(transaction -> transaction.getOtherParty().equals(otherParty)).
                filter(transaction -> !Common.isEmpty(transaction.getTag())).
                sorted(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction o1, Transaction o2) {
                        return o2.getTransactionDate().compareTo(o1.getTransactionDate());
                    }
                }).findFirst().
                map(transactionProperty).
                orElse(defaulValue);
    }
}
