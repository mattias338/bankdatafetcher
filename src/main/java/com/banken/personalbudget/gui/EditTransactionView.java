package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class EditTransactionView {
    private Transaction transaction;
    private Data data;
    private Storer storer;

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Node getView() {
        VBox vbox = new VBox();
        ObservableList<Node> children = vbox.getChildren();

        NewTransactionManager newTransactionManager = new NewTransactionManager(data, transaction, storer);
        NewTransactionGuiElements newTransactionGuiElements = new NewTransactionGuiElements(newTransactionManager);

        children.addAll(newTransactionGuiElements.getStaticTexts());

        TextArea description = new TextArea();
        description.setWrapText(true);
        description.setPromptText("Description");
        children.add(description);

        newTransactionGuiElements.setDescriptionTextInputControl(description);

        children.add(newTransactionGuiElements.getTag());
        children.add(newTransactionGuiElements.getTreatAsIncomeCheckBox());
        children.add(newTransactionGuiElements.getIgnoreEntryCheckBox());
        children.add(newTransactionGuiElements.getSaveButton());
        return vbox;
    }

    public void setStorer(Storer storer) {
        this.storer = storer;
    }
}
