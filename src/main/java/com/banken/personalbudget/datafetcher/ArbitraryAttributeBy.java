package com.banken.personalbudget.datafetcher;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

import java.util.List;

public class ArbitraryAttributeBy extends By {
    private final String attributeName;
    private final String attributeValue;

    public ArbitraryAttributeBy(String attributeName, String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return ((FindsByXPath) context).findElementsByXPath(".//*[@" + attributeName + " = '" + attributeValue + "']");
    }
}
