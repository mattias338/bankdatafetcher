package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.data.IncomeExpense;
import com.banken.personalbudget.data.YearQuarter;
import com.banken.personalbudget.datafetcher.Transaction;
import com.banken.personalbudget.gui.tableview.TableViewLineData;
import com.banken.personalbudget.gui.tableview.TopLevelTagLineData;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.banken.personalbudget.Common.removeDecimalsIfDouble;

public class QuarterlyView extends TimeSpanView {
    private static final DecimalFormat DF = new DecimalFormat("0.0");

    private Data data;
    private QuarterlyViewData quarterly;
    private List<TableViewLineData> tableLineData;

    private List<Set<YearMonth>> yearMonthsGroups = new ArrayList<>();

    private boolean combineAllCompleteMonths;

    public void setData(Data data) {
        this.data = data;
        quarterly = new QuarterlyViewData();
        quarterly.setData(data);
        quarterly.init();
        tableLineData = quarterly.getTableLineData();
    }

    public Node getView() {
        if (combineAllCompleteMonths) {
            Set<YearMonth> allYearMonths = new HashSet<>(quarterly.getYearMonths());
            YearMonth now = YearMonth.now();
            allYearMonths.remove(now);
            yearMonthsGroups.add(allYearMonths);
        } else {
            quarterly.getQuarters().forEach(yearQuarter -> yearMonthsGroups.add(yearQuarter.getYearMonths()));
        }


        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);

        addColumnHeaderWithTags(gridPane);
        addRowHeaderWithYearMonths(gridPane);
        fillData(gridPane);

        return gridPane;
    }

    public void setCombineAllCompleteMonths(boolean combineAllCompleteMonths) {
        this.combineAllCompleteMonths = combineAllCompleteMonths;
    }

    private void fillData(GridPane gridPane) {
        int column = 1; // skip first column
        for (Set<YearMonth> yearMonthGroup: yearMonthsGroups) {

            Predicate<Transaction> matchingYearMonthsPredicate = QuarterlyViewData.getMatchingQuarterPredicate(yearMonthGroup);

            int row = 1; // skip first row
            for (TableViewLineData lineData : tableLineData) {
                String amount = lineData.getData(matchingYearMonthsPredicate);
                amount = removeDecimalsIfDouble(amount);
                Text amountText = new Text(amount);
                if (lineData.isDataClickable()) {
                    amountText.setOnMouseClicked(new TableDataMouseEventHandler(yearMonthGroup, lineData.getHeader(), this.data));
                }
                GridPane.setHalignment(amountText, HPos.RIGHT);
                gridPane.add(amountText, column, row);

                String expenseRatio = "";
                String incomeRatio = "";
                if (lineData instanceof TopLevelTagLineData) {
                    TopLevelTagLineData topLevelTagLineData = (TopLevelTagLineData) lineData;
                    if (topLevelTagLineData.getIncomeExpense() == IncomeExpense.EXPENSE) {
                        expenseRatio = formatted(topLevelTagLineData.ofExpenseRatio(matchingYearMonthsPredicate));
                        incomeRatio = formatted(topLevelTagLineData.ofIncomeRatio(matchingYearMonthsPredicate));
                    }
                }
                Text expenseRatioText = new Text(expenseRatio);
                GridPane.setHalignment(expenseRatioText, HPos.RIGHT);
                gridPane.add(expenseRatioText, column + 1, row);

                Text incomeRatioText = new Text(incomeRatio);
                GridPane.setHalignment(incomeRatioText, HPos.RIGHT);
                gridPane.add(incomeRatioText, column + 2, row);

                row++;
            }
            column += 3;
        }
    }

    private void addRowHeaderWithYearMonths(GridPane gridPane) {
        List<YearQuarter> yearQuarters = quarterly.getQuarters();

        int column = 1; // skip first column
        for (Set<YearMonth> yearMonthGroup : yearMonthsGroups) {
            String label = "";
            List<Integer> years = yearMonthGroup.stream().map(YearMonth::getYear).distinct().collect(Collectors.toList());
            for (Integer year : years) {
                String monthsOfYear = yearMonthGroup.stream().
                        filter(yearMonth -> yearMonth.getYear() == year).
                        map(yearMonth -> yearMonth.getMonth().getValue()).
                        map(value -> "" + value).
                        collect(Collectors.joining(","));
                label += year + "-" + monthsOfYear + "; ";
            }
            gridPane.add(new Text(label), column++, 0);
            gridPane.add(new Text("Exp ratio"), column++, 0);
            gridPane.add(new Text("Inc ratio"), column++, 0);
        }

    }

    private void addColumnHeaderWithTags(GridPane gridPane) {
        List<String> allTags = tableLineData.stream().map(TableViewLineData::getHeader).collect(Collectors.toList());

        int row = 1; // skip first row
        for (String tag : allTags) {
            gridPane.add(new Text(tag), 0, row++);
        }
    }

    private String formatted(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return "NA";
        }
        return DF.format(100 * Math.abs(d));
    }

    public static void main(String[] args) {
        System.out.println(DF.format(3.0031));
        System.out.println(DF.format(323));
        System.out.println(DF.format(0.0031));
        System.out.println(DF.format(3.1431));
        System.out.println(DF.format(3.2));
    }
}
