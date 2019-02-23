package com.banken.assets;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class StockXmlParseHelperTest {

    private static final String EXAMPLE_TEXT = "";

    private static final String CORRECT_VALUE = "78,88";

    @Test
    public void testProcessLine() throws Exception {
        StockXmlParseHelper stockXmlParseHelper = new StockXmlParseHelper();
        String[] split = EXAMPLE_TEXT.split("\\n");
        for (int i = 0; i < split.length; i++) {
            boolean foundValue = stockXmlParseHelper.processLine(split[i]);
            if (foundValue) {
                System.out.println(split[i]);
                Assert.assertTrue(split[i].contains(CORRECT_VALUE));
                return;
            }
        }
        throw new Exception("Couldn't find value");
    }

}