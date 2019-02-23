package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class EditEntryDialogProvider {

    private Storer storer;
    private Data data;

    public Dialog<Void> getDialog(Transaction transaction) {
        Dialog<Void> editEntryDialog = new Dialog<>();
        editEntryDialog.setTitle("Edit transaction");

        EditTransactionView editTransactionView = new EditTransactionView();
        editTransactionView.setData(data);
        editTransactionView.setStorer(storer);
        editTransactionView.setTransaction(transaction);
        Node editEntryView = editTransactionView.getView();

        editEntryDialog.getDialogPane().setContent(editEntryView);
        editEntryDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        return editEntryDialog;
    }

    public void setStorer(Storer storer) {
        this.storer = storer;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
