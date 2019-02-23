package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.YearQuarter;
import com.banken.personalbudget.data.Data;
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

    public TableDataMouseEventHandler(YearQuarter yearQuarter, String topLevelTag, Data data) {
        this(yearQuarter.getYearMonths(), topLevelTag, data);
    }

    public TableDataMouseEventHandler(YearMonth yearMonth, String topLevelTag, Data data) {
        this(Collections.singleton(yearMonth), topLevelTag, data);
    }

    public TableDataMouseEventHandler(Set<YearMonth> yearMonths, String topLevelTag, Data data) {
        this.yearMonths = yearMonths;
        this.topLevelTag = topLevelTag;
        this.data = data;
    }

    @Override
    public void handle(MouseEvent event) {
        EntryGridView entryGridView = new EntryGridView();
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
        Node node = entryGridView.createView();
        entryGridView.setData(data);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detailed view: tag: " + topLevelTag + ", year month: " + yearMonths);
        dialog.getDialogPane().setContent(node);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.show();

    }
}
