package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;

import java.util.Optional;

public class NewTransactionManager {
    private Data data;
    private Transaction transaction;
    private final Storer storer;

    public NewTransactionManager(Data data, Transaction transaction, Storer storer) {
        this.data = data;
        this.transaction = transaction;
        this.storer = storer;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Data getData() {
        return data;
    }

    private String getSuggestedTag() {
        return data.getLatestTagResolver().resolve(transaction.getOtherParty());
    }

    public boolean hasSuggestedTag() {
        return getSuggestedTag() != null;
    }

    public String getTag() {
        if (transaction.isNewTransaction()) {
            return getSuggestedTag();
        } else {
            return transaction.getTag();
        }
    }

    private boolean isSuggestedTreatAsIncome() {
        Boolean suggestedTreatAsIncome = data.getLatestTreatAsIncomeResolver().resolve(transaction.getOtherParty());
        return Optional.ofNullable(suggestedTreatAsIncome).orElse(false);
    }

    public boolean isTreatAsIncome() {
        if (transaction.isNewTransaction()) {
            return isSuggestedTreatAsIncome();
        } else {
            return transaction.isTreatAsIncome();
        }
    }

    private boolean isSuggestedIgnore() {
        return data.getLatestIgnoreResolver().resolve(transaction.getOtherParty());
    }

    public boolean isIgnore() {
        if (transaction.isNewTransaction()) {
            return isSuggestedIgnore();
        } else {
            return transaction.isIgnore();
        }
    }

    public void save() {
        storer.store();
    }

    @Override
    public String toString() {
        return "NewTransactionManager{" +
                "data=" + data +
                ", transaction=" + transaction +
                ", storer=" + storer +
                '}';
    }
}
