package com.banken.personalbudget.gui.tableview;

import com.banken.personalbudget.datafetcher.Transaction;

import java.util.function.Predicate;

public class EmptyLineData implements TableViewLineData {
    public static final EmptyLineData INSTANCE = new EmptyLineData();

    @Override
    public String getHeader() {
        return "";
    }

    @Override
    public String getData(Predicate<Transaction> timePredicate) {
        return "";
    }
}
