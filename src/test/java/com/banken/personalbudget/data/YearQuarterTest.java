package com.banken.personalbudget.data;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;

public class YearQuarterTest {

    private YearQuarter yearQuarter;

    @BeforeClass
    public void init() {
        yearQuarter = new YearQuarter(2018, 1);

    }

    @Test
    public void positiveTests() throws Exception {
        Assert.assertTrue(yearQuarter.isDateInQuarter(LocalDate.of(2018, 1, 1)));
        Assert.assertTrue(yearQuarter.isDateInQuarter(LocalDate.of(2018, 1, 31)));
        Assert.assertTrue(yearQuarter.isDateInQuarter(LocalDate.of(2018, 2, 1)));
        Assert.assertTrue(yearQuarter.isDateInQuarter(LocalDate.of(2018, 2, 28)));
        Assert.assertTrue(yearQuarter.isDateInQuarter(LocalDate.of(2018, 3, 1)));
        Assert.assertTrue(yearQuarter.isDateInQuarter(LocalDate.of(2018, 3, 31)));
    }

    @Test
    public void negativeTests() throws Exception {
        Assert.assertFalse(yearQuarter.isDateInQuarter(LocalDate.of(2017, 12, 31)));
        Assert.assertFalse(yearQuarter.isDateInQuarter(LocalDate.of(2000, 1, 1)));
        Assert.assertFalse(yearQuarter.isDateInQuarter(LocalDate.of(2018, 4, 1)));
        Assert.assertFalse(yearQuarter.isDateInQuarter(LocalDate.of(2018, 4, 30)));
    }

}