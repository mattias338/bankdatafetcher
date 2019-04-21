package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.data.YearQuarter;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Set;

public class TableDataMouseEventHandler implements EventHandler<MouseEvent> {

    private Set<YearMonth> yearMonths;
    private String topLevelTag;
    private Data data;
    private Storer storer;

    public TableDataMouseEventHandler(YearQuarter yearQuarter, String topLevelTag, Data data, Storer storer) {
        this(yearQuarter.getYearMonths(), topLevelTag, data, storer);
    }

    public TableDataMouseEventHandler(YearMonth yearMonth, String topLevelTag, Data data, Storer storer) {
        this(Collections.singleton(yearMonth), topLevelTag, data, storer);
    }

    public TableDataMouseEventHandler(Set<YearMonth> yearMonths, String topLevelTag, Data data, Storer storer) {
        this.yearMonths = yearMonths;
        this.topLevelTag = topLevelTag;
        this.data = data;
        this.storer = storer;
    }

    @Override
    public void handle(MouseEvent event) {
        AllEntriesGridView entryGridView = new AllEntriesGridView(storer, data);
        entryGridView.setFilter(transaction -> {
            if (transaction.isIgnore()) {
                return false;
            }
            LocalDate dateDate = Data.getDateDate(transaction.getTransactionDate());
            YearMonth transactionYearMonth = YearMonth.from(dateDate);
            if (!yearMonths.contains(transactionYearMonth)) {
                return false;
            }
            if (!transaction.getTag().startsWith(topLevelTag)) {
                return false;
            }

            return true;
        });
        entryGridView.setData(data);

        Node node = entryGridView.createView();

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detailed view: tag: " + topLevelTag + ", year month: " + yearMonths);
        dialog.getDialogPane().setContent(node);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.show();

    }
}
