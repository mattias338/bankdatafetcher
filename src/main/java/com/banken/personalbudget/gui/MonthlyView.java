package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.gui.tableview.TableViewLineData;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static com.banken.personalbudget.Common.removeDecimalsIfDouble;

public class MonthlyView extends TimeSpanView {
    private Data data;
    private MonthlyViewData monthlyViewData;
    private List<TableViewLineData> tableLineData;
    private Storer storer;

    public void setData(Data data) {
        this.data = data;
        monthlyViewData = new MonthlyViewData();
        monthlyViewData.setData(data);
        monthlyViewData.init();
        tableLineData = monthlyViewData.getTableLineData();
    }

    public Node getView() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        addColumnHeaderWithTags(gridPane);
        addRowHeaderWithYearMonths(gridPane);
        fillData(gridPane);


        return gridPane;


    }

    private void fillData(GridPane gridPane) {
        List<YearMonth> yearMonths = monthlyViewData.getYearMonths();

        int column = 1; // skip first column
        for (YearMonth yearMonth : yearMonths) {
            int row = 1; // skip first row
            for (TableViewLineData lineData : tableLineData) {
                String amount = lineData.getData(MonthlyViewData.getMatchingYearMonthPredicate(yearMonth));
                amount = removeDecimalsIfDouble(amount);
                Text text = new Text(amount);
                if (lineData.isDataClickable()) {
                    text.setOnMouseClicked(new TableDataMouseEventHandler(yearMonth, lineData.getHeader(), this.data, storer));
                }
                GridPane.setHalignment(text, HPos.RIGHT);
                gridPane.add(text, column, row++);
            }
            column++;
        }

    }

    private void addRowHeaderWithYearMonths(GridPane gridPane) {
        List<YearMonth> yearMonths = monthlyViewData.getYearMonths();

        int column = 1; // skip first column
        for (YearMonth yearMonth : yearMonths) {
            gridPane.add(new Text(yearMonth.toString()), column++, 0);
        }

    }

    private void addColumnHeaderWithTags(GridPane gridPane) {
        List<String> allTags = tableLineData.stream().map(TableViewLineData::getHeader).collect(Collectors.toList());

        int row = 1; // skip first row
        for (String tag : allTags) {
            gridPane.add(new Text(tag), 0, row++);
        }
    }

    public void setStorer(Storer storer) {
        this.storer = storer;
    }
}
