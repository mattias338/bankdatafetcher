package com.banken.assets;

public class StockXmlParseHelper {
    private boolean firstLastFound;
    private int rowsAfterFirstLast;

    private String value;

    public boolean processLine(String line) {
        if (!firstLastFound) {
            if (line.contains("first last")) {
                firstLastFound = true;
            }
            return false;
        }
        rowsAfterFirstLast++;
        if (rowsAfterFirstLast == 3) {
            value = line.trim().substring(4);
            value = value.substring(0, value.indexOf('<'));
            return true;
        }
        return false;
    }

    public String getValue() {
        return value;
    }
}
