package com.banken.personalbudget.data;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.util.HashSet;
import java.util.Set;

public class YearQuarter {
    private int quarter;
    private int year;

    public YearQuarter(LocalDate localDate) {
        quarter = localDate.get(IsoFields.QUARTER_OF_YEAR);
        year = localDate.getYear();
    }

    public YearQuarter(int year, int quarter) {
        this.quarter = quarter;
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public int getYear() {
        return year;
    }

    public Set<YearMonth> getYearMonths() {
        Set<YearMonth> yearMonths = new HashSet<>();
        yearMonths.add(YearMonth.of(year, (quarter - 1)*3 + 1));
        yearMonths.add(YearMonth.of(year, (quarter - 1)*3 + 2));
        yearMonths.add(YearMonth.of(year, (quarter - 1)*3 + 3));
        return yearMonths;
    }

    public LocalDate firstDate() {
        return LocalDate.of(year, (quarter - 1)*3 + 1, 1);
    }

    public LocalDate lastDate() {
        LocalDate firstDayOfLastMonth = LocalDate.of(year, (quarter - 1) * 3 + 3, 1);
        return firstDayOfLastMonth.withDayOfMonth(firstDayOfLastMonth.lengthOfMonth());
    }

//    @Override
//    public String toString() {
//        return year + "Q" + quarter;
//    }

    public boolean isDateInQuarter(LocalDate date) {
        return !date.isBefore(firstDate()) && !date.isAfter(lastDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YearQuarter that = (YearQuarter) o;

        if (quarter != that.quarter) return false;
        return year == that.year;
    }

    @Override
    public int hashCode() {
        int result = quarter;
        result = 31 * result + year;
        return result;
    }

    public String getName() {
        return year + "Q" + quarter;
    }
}
