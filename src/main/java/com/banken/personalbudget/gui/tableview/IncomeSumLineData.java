package com.banken.personalbudget.gui.tableview;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;

import java.util.function.Predicate;

public class IncomeSumLineData implements TableViewLineData {
    private static final String INCOME_SUM = "Income sum";
    private Data data;

    public IncomeSumLineData(Data data) {
        this.data = data;
    }

    @Override
    public String getHeader() {
        return INCOME_SUM;
    }

    @Override
    public String getData(Predicate<Transaction> timePredicate) {
        return "" + data.getIncomeSum(timePredicate);
    }
}
