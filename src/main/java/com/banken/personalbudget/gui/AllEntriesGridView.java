package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;
import javafx.scene.Node;
import javafx.scene.control.Dialog;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class AllEntriesGridView extends EntryGridView {
    private static final Predicate<Transaction> DEFAULT_FILTER = transaction -> !transaction.isIgnore();
    private final EditEntryDialogProvider editEntryDialogProvider;

    public AllEntriesGridView(Storer storer, Data data) {
        super(data);
        setFilter(DEFAULT_FILTER);
        editEntryDialogProvider = new EditEntryDialogProvider();
        editEntryDialogProvider.setStorer(storer);
        editEntryDialogProvider.setData(data);
    }

    @Override
    protected List<String> getHeaders() {
        return Transaction.getNames();
    }

    @Override
    protected Function<Transaction, List<? extends Node>> getTableContentProvider() {
        return transaction -> {
            Dialog editTransactionDialog = editEntryDialogProvider.getDialog(transaction);
            return EntryGridView.convertStringsToTextArray(transaction.getValues(), editTransactionDialog);
        };
    }
}
