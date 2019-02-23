package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.List;
import java.util.function.Predicate;

public class EntryGridView {
    private static final Predicate<Transaction> DEFAULT_FILTER = transaction -> !transaction.isIgnore();

    private Data data;
    private GridPane gridPane;
    private Storer storer;
    private Predicate<Transaction> filter = DEFAULT_FILTER;

    public Node createView() {
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(1);


        List<String> names = Transaction.getNames();
        Text[] headers = names.stream().
                map(Text::new).
                toArray(Text[]::new);
        // Add headers
        gridPane.addRow(0, headers);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);

        return scrollPane;
    }

    public void setData(Data data) {
        this.data = data;

        // rowindex 0 is for headers
        int rowindex = 1;
        for (Transaction transaction : data.getTransactions(filter)) {
            EditEntryDialogProvider editEntryDialogProvider = new EditEntryDialogProvider();
            editEntryDialogProvider.setStorer(storer);
            editEntryDialogProvider.setData(data);
            Dialog editTransactionDialog = editEntryDialogProvider.getDialog(transaction);
            gridPane.addRow(rowindex++, convertStringsToTextArray(transaction.getValues(), editTransactionDialog));
        }
    }

    public static Text[] convertStringsToTextArray(List<String> strings, Dialog action) {
        return strings.stream().
                map(s -> createClickableText(s, action)).
                toArray(Text[]::new);
    }

    public static Text createClickableText(String s, Dialog action) {
        Text text = new Text(s);
        text.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                action.show();
            }
        });
        return text;
    }

    public void setStorer(Storer storer) {
        this.storer = storer;
    }

    public void setFilter(Predicate<Transaction> filter) {
        this.filter = filter;
    }
}
