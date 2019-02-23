package com.banken.personalbudget.gui.tableview;

import com.banken.personalbudget.datafetcher.Transaction;

import java.util.function.Predicate;

public interface TableViewLineData {
    String getHeader();

    String getData(Predicate<Transaction> timePredicate);

    default boolean isDataClickable() {
        return false;
    }
}
