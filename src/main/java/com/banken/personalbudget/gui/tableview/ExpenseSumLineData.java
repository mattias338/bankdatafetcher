package com.banken.personalbudget.gui.tableview;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;

import java.util.function.Predicate;

public class ExpenseSumLineData implements TableViewLineData {
    private static final String EXPENSE_SUM = "Expense sum";
    private Data data;

    public ExpenseSumLineData(Data data) {
        this.data = data;
    }

    @Override
    public String getHeader() {
        return EXPENSE_SUM;
    }

    @Override
    public String getData(Predicate<Transaction> timeInterval) {
        return "" + data.getExpenseSum(timeInterval);
    }
}
