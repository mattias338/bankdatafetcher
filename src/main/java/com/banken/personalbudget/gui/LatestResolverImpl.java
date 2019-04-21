package com.banken.personalbudget.gui;

import com.banken.personalbudget.Common;
import com.banken.personalbudget.datafetcher.Transaction;

import java.util.List;
import java.util.function.Function;

public class LatestResolverImpl<T> implements LatestResolver<T> {
    private final List<Transaction> transactions;
    private final Function<Transaction, T> transactionProperty;
    private T defaultValue = null;


    public LatestResolverImpl(List<Transaction> transactions, Function<Transaction, T> transactionProperty) {
        this.transactions = transactions;
        this.transactionProperty = transactionProperty;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public T resolve(String otherParty) {
        return transactions.stream().
                filter(transaction -> transaction.getOtherParty().equals(otherParty)).
                filter(transaction -> !Common.isEmpty(transaction.getTag())).
                min((o1, o2) -> o2.getTransactionDate().compareTo(o1.getTransactionDate())).
                map(transactionProperty).
                orElse(defaultValue);
    }
}
