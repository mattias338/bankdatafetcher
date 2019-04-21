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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class EntryGridView {

    private Data data;
    private GridPane gridPane;
    private Predicate<Transaction> filter = transaction -> true;

    public EntryGridView(Data data) {
        this.data = data;
    }

    public Node createView() {
        createGrid();

        addHeaders();

        addContent();

        return wrapInScrollPane();
    }

    private void addContent() {
        // rowindex 0 is for headers
        int rowindex = 1;
        for (Transaction transaction : data.getTransactions(filter)) {
            List<? extends Node> nodes = getTableContentProvider().apply(transaction);
            if (nodes == null) {
                continue;
            }
            Node[] nodesArray = nodes.toArray(new Node[0]);
            gridPane.addRow(rowindex++, nodesArray);
        }
    }

    private void addHeaders() {
        Text[] headers = getHeaders().stream().
                map(Text::new).
                toArray(Text[]::new);
        // Add headers
        gridPane.addRow(0, headers);
    }

    protected abstract List<String> getHeaders();

    protected abstract Function<Transaction, List<? extends Node>> getTableContentProvider();

    public void setData(Data data) {
        this.data = data;

    }

    public static List<Text> convertStringsToTextArray(List<String> strings, Dialog action) {
        return strings.stream().
                map(s -> createClickableText(s, action)).
                collect(Collectors.toList());//toArray(Text[]::new);
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


    public void setFilter(Predicate<Transaction> filter) {
        this.filter = filter;
    }

    private ScrollPane wrapInScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        return scrollPane;
    }

    private void createGrid() {
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(1);

    }
}
