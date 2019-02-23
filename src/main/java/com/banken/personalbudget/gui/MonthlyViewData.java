package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.datafetcher.Transaction;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MonthlyViewData extends ViewDataBase {


    public static Predicate<Transaction> getMatchingYearMonthPredicate(YearMonth otherYearMonth) {
        return transaction -> {
            String transactionDate = transaction.getTransactionDate();
            LocalDate dateDate = Data.getDateDate(transactionDate);
            return YearMonth.from(dateDate).equals(otherYearMonth);
        };
    }

//    public String getAmountSum2Decimals(YearMonth yearMonth, String topLevelTag) {
//        String amountSum = getAmountSum(yearMonth, topLevelTag);
//        if (!amountSum.equals("")) {
//            double v = Double.parseDouble(amountSum);
//            NumberFormat numberFormat = DecimalFormat.getInstance();
//            numberFormat.setMinimumFractionDigits(2);
//            numberFormat.setMaximumFractionDigits(2);
//            amountSum = numberFormat.format(v);
//        }
//        return amountSum;
//    }

}
