package com.banken.personalbudget.data;

import com.banken.personalbudget.Common;
import com.banken.personalbudget.datafetcher.Transaction;
import com.banken.personalbudget.gui.LatestResolver;
import com.banken.personalbudget.gui.LatestResolverImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Data {
    private List<Transaction> transactions;

    public static LocalDate getDateDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Transaction> getNotIgnoredTransactions() {
        return getTransactions(transaction -> !transaction.isIgnore());
    }

    public List<Transaction> getTransactions(Predicate<Transaction> filter) {
        return transactions.stream().
                filter(filter).
                collect(Collectors.toList());
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Data data = (Data) o;

        return transactions != null ? transactions.equals(data.transactions) : data.transactions == null;
    }

    @Override
    public int hashCode() {
        return transactions != null ? transactions.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Data{" +
                "transactions=" + transactions +
                '}';
    }

    public List<Transaction> getNewTransactions() {
        return transactions.stream().
                filter(Transaction::isNewTransaction).
                filter(transaction -> !transaction.isIgnore()).
                collect(Collectors.toList());
    }

    public List<String> getAllTags() {
        List<String> allTagsFiltered = getAllXFiltered(Transaction::getTag, transaction -> true);
        allTagsFiltered.sort(String::compareTo);
        return allTagsFiltered;

    }

    private List<String> getAllTopLevelTags() {
        return getAllXFiltered(Transaction::getTopLevelTag, transaction -> true);
    }

    private <T> List<T> getAllXFiltered(Function<Transaction, T> transactionProperty, Predicate<Transaction> filter) {
        return transactions.stream().
                filter(filter).
                map(transactionProperty).
                distinct().
                filter(tag -> !Common.isEmpty(tag)).
                collect(Collectors.toList());
    }

    public List<String> getIncomeTags() {
        return getAllXFiltered(Transaction::getTag, Transaction::isTreatAsIncome);
    }

    public List<String> getTopLevelIncomeTags() {
        return getAllXFiltered(Transaction::getTopLevelTag, Transaction::isTreatAsIncome);
    }

    public List<String> getExpenseTags() {
        List<String> tags = getAllTags();
        tags.removeAll(getIncomeTags());
        return tags;

    }

    public List<String> getTopLevelExpenseTags() {
        List<String> tags = getAllTopLevelTags();
        tags.removeAll(getTopLevelIncomeTags());
        return tags;
    }

    public LatestResolver<String> getLatestTagResolver() {
        LatestResolver<String> latestTagResolver =
                new LatestResolverImpl<>(transactions, Transaction::getTag);
        return latestTagResolver;
    }

    public LatestResolver<Boolean> getLatestTreatAsIncomeResolver() {
        LatestResolverImpl<Boolean> latestTreatAsIncomeResolver =
                new LatestResolverImpl<>(transactions, Transaction::isTreatAsIncome);
        latestTreatAsIncomeResolver.setDefaultValue(false);
        return latestTreatAsIncomeResolver;
    }

    public LatestResolverImpl<Boolean> getLatestIgnoreResolver() {
        LatestResolverImpl<Boolean> latestIgnoreResolver =
                new LatestResolverImpl<>(transactions, Transaction::isIgnore);
        latestIgnoreResolver.setDefaultValue(false);
        return latestIgnoreResolver;
    }

    public double getExpenseSum(Predicate<Transaction> timeInterval) {
        return getNotIgnoredTransactions().stream().
                filter(timeInterval).
                filter(transaction -> getExpenseTags().contains(transaction.getTag())).
                mapToDouble(Data::parseDouble).
                sum();
    }

    public double getIncomeSum(Predicate<Transaction> timeInterval) {
        return getNotIgnoredTransactions().stream().
                filter(timeInterval).
                filter(transaction -> getIncomeTags().contains(transaction.getTag())).
                mapToDouble(Data::parseDouble).
                sum();
    }

    public static double parseDouble(Transaction transaction) {
        String amountStringCleared = transaction.getAmount().replaceAll(" ", "").replaceAll(",", ".");
        return Double.parseDouble(amountStringCleared);
    }
}