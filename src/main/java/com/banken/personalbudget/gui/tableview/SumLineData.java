package com.banken.personalbudget.gui.tableview;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;

import java.util.function.Predicate;

public class SumLineData implements TableViewLineData {
    private static final String SUM = "Sum";
    private final Data data;

    public SumLineData(Data data) {
        this.data = data;
    }

    @Override
    public String getHeader() {
        return SUM;
    }

    @Override
    public String getData(Predicate<Transaction> timePredicate) {
        return "" + data.getNotIgnoredTransactions().stream().
                filter(timePredicate).
                mapToDouble(Data::parseDouble).
                sum();
    }
}
