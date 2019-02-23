package com.banken.personalbudget.gui.tableview;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;

import java.util.function.Predicate;

public class SavingsRatioLineData implements TableViewLineData {
    private static final String SAVINGS_RATIO = "Savings ratio (in %)";
    private final Data data;

    private ExpenseSumLineData expenseSumLineData;
    private IncomeSumLineData incomeSumLineData;

    public SavingsRatioLineData(Data data) {
        this.data = data;
    }

    public void setExpenseSumLineData(ExpenseSumLineData expenseSumLineData) {
        this.expenseSumLineData = expenseSumLineData;
    }

    public void setIncomeSumLineData(IncomeSumLineData incomeSumLineData) {
        this.incomeSumLineData = incomeSumLineData;
    }

    @Override
    public String getHeader() {
        return SAVINGS_RATIO;
    }

    @Override
    public String getData(Predicate<Transaction> timePredicate) {
        int savingsRate = (int) ((1 - (Math.abs(Double.parseDouble(expenseSumLineData.getData(timePredicate)))
                / Double.parseDouble(incomeSumLineData.getData(timePredicate)))) * 100);

        if (savingsRate < 0) {
            return "NA";
        }
        return "" + savingsRate;

    }
}
