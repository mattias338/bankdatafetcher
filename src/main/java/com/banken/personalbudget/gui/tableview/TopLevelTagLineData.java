package com.banken.personalbudget.gui.tableview;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.data.IncomeExpense;
import com.banken.personalbudget.datafetcher.Transaction;

import java.util.function.Predicate;

public class TopLevelTagLineData implements TableViewLineData {
    private final String tag;
    private Data data;
    private IncomeExpense incomeExpense;

    public TopLevelTagLineData(String tag, Data data, IncomeExpense incomeExpense) {
        this.tag = tag;
        this.data = data;
        this.incomeExpense = incomeExpense;
    }

    public IncomeExpense getIncomeExpense() {
        return incomeExpense;
    }

    @Override
    public String getHeader() {
        return tag;
    }

    @Override
    public String getData(Predicate<Transaction> timePredicate) {
        return "" + getDataDouble(timePredicate);
    }

    private double getDataDouble(Predicate<Transaction> timePredicate) {
        return data.getNotIgnoredTransactions().stream().
                filter(timePredicate).
                filter(transaction ->
                {
                    String topLevelTag = transaction.getTopLevelTag();
                    if (topLevelTag == null) {
                        throw new RuntimeException("Missing topLevelTag for " + transaction);
                    }
                    return topLevelTag.equals(tag);
                }).
                mapToDouble(Data::parseDouble).
                sum();
    }

    @Override
    public boolean isDataClickable() {
        return true;
    }

    public double ofIncomeRatio(Predicate<Transaction> timePredicate) {
        double amount = getDataDouble(timePredicate);
        return amount / data.getIncomeSum(timePredicate);
    }

    public double ofExpenseRatio(Predicate<Transaction> timePredicate) {
        double amount = getDataDouble(timePredicate);
        return amount / data.getExpenseSum(timePredicate);
    }
}
