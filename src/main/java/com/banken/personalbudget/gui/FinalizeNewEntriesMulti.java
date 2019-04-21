package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class FinalizeNewEntriesMulti extends EntryGridView {
    private static final Predicate<Transaction> ONLY_NEW = Transaction::isNewTransaction;
    private final EditEntryDialogProvider editEntryDialogProvider;

    private Storer storer;
    private Data data;

    private List<NewTransactionGuiElements> newTransactionsShown = new ArrayList<NewTransactionGuiElements>();

    public FinalizeNewEntriesMulti(Storer storer, Data data) {
        super(data);
        editEntryDialogProvider = new EditEntryDialogProvider();
        editEntryDialogProvider.setStorer(storer);
        editEntryDialogProvider.setData(data);
        setFilter(ONLY_NEW);
        this.data = data;
        this.storer = storer;
    }

    @Override
    public Node createView() {
        VBox outer = new VBox();

        HBox controllers = new HBox();

        Button saveAll = new Button("Save all");
        saveAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newTransactionsShown.forEach(NewTransactionGuiElements::save);
            }
        });
        controllers.getChildren().add(saveAll);

        Node grid = super.createView();

        outer.getChildren().addAll(controllers, grid);

        return outer;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setStorer(Storer storer) {
        this.storer = storer;
    }

    @Override
    protected List<String> getHeaders() {
        ArrayList<String> copy = new ArrayList<>(Transaction.getNames());
        copy.add(0, "New?");
        copy.add("Ignore?");
        return copy;
    }

    @Override
    protected Function<Transaction, List<? extends Node>> getTableContentProvider() {
        return transaction -> {
            NewTransactionManager newTransactionManager = new NewTransactionManager(data, transaction, storer);
            if (!newTransactionManager.hasSuggestedTag()) {
                return null;
            }
            NewTransactionGuiElements newTransactionGuiElements = new NewTransactionGuiElements(newTransactionManager);
            newTransactionsShown.add(newTransactionGuiElements);

            List<Node> nodes = new ArrayList<>();


            List<String> staticValuesCopy = new ArrayList<>(transaction.getStaticValues());
            String newText = transaction.isNewTransaction() ? "New: " : "Saved";
            staticValuesCopy.add(0, newText);
            Dialog editTransactionDialog = editEntryDialogProvider.getDialog(transaction);
            nodes.addAll(EntryGridView.convertStringsToTextArray(staticValuesCopy, editTransactionDialog));


            TextField textField = new TextField();
            textField.setPromptText("Description");
            newTransactionGuiElements.setDescriptionTextInputControl(textField);
            nodes.add(textField);

            nodes.add(newTransactionGuiElements.getTag());
            nodes.add(newTransactionGuiElements.getTreatAsIncomeCheckBox());
            nodes.add(newTransactionGuiElements.getIgnoreEntryCheckBox());

            return nodes;
        };
    }
}
