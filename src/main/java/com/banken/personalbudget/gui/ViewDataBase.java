package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.data.IncomeExpense;
import com.banken.personalbudget.gui.tableview.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewDataBase {
    protected Data data;
    private List<TableViewLineData> lines = new ArrayList<>();

    public void setData(Data data) {
        this.data = data;
    }

    public void init() {
        addLines();
    }

    public List<TableViewLineData> getTableLineData() {
        return lines;
    }

    public List<YearMonth> getYearMonths() {
        return data.getNotIgnoredTransactions().stream().
                map(transaction -> transaction.getTransactionDate()).
                map(Data::getDateDate).
                map(date -> YearMonth.from(date)).
                distinct().
                sorted((ym1, ym2) -> ym1.isBefore(ym2) ? -1 : 1).
                collect(Collectors.toList());
    }

    private void addLines() {
        IncomeSumLineData incomeSumLineData = new IncomeSumLineData(data);
        ExpenseSumLineData expenseSumLineData = new ExpenseSumLineData(data);

        data.getTopLevelIncomeTags().forEach(tag -> lines.add(new TopLevelTagLineData(tag, data, IncomeExpense.INCOME)));
        lines.add(EmptyLineData.INSTANCE);
        lines.add(incomeSumLineData);
        lines.add(EmptyLineData.INSTANCE);
        data.getTopLevelExpenseTags().forEach(tag -> lines.add(new TopLevelTagLineData(tag, data, IncomeExpense.EXPENSE)));
        lines.add(EmptyLineData.INSTANCE);
        lines.add(expenseSumLineData);
        lines.add(EmptyLineData.INSTANCE);
        lines.add(new SumLineData(data));
        lines.add(EmptyLineData.INSTANCE);
        SavingsRatioLineData savingsRatioLineData = new SavingsRatioLineData(data);
        savingsRatioLineData.setExpenseSumLineData(expenseSumLineData);;
        savingsRatioLineData.setIncomeSumLineData(incomeSumLineData);
        lines.add(savingsRatioLineData);
    }
}
