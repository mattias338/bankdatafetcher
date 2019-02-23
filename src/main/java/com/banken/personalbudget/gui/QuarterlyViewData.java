package com.banken.personalbudget.gui;

import com.banken.personalbudget.data.Data;
import com.banken.personalbudget.data.YearQuarter;
import com.banken.personalbudget.datafetcher.Transaction;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class QuarterlyViewData extends ViewDataBase {

    public List<YearQuarter> getQuarters() {
        return data.getNotIgnoredTransactions().stream().
                map(transaction -> transaction.getTransactionDate()).
                map(Data::getDateDate).
                map(date -> getYearQuarter(date)).
                distinct().
                collect(Collectors.toList());
    }

    public static Predicate<Transaction> getMatchingQuarterPredicate(Set<YearMonth> yearMonths) {
        return transaction -> {
            String transactionDate = transaction.getTransactionDate();
            LocalDate dateDate = Data.getDateDate(transactionDate);
            return yearMonths.stream().anyMatch(yearMonth ->
                    dateDate.getMonth() == yearMonth.getMonth() && dateDate.getYear() == yearMonth.getYear());
        };
    }

    private YearQuarter getYearQuarter(LocalDate date) {
        return new YearQuarter(date);
    }
}
