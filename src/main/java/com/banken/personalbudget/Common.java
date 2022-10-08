package com.banken.personalbudget;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Common {
    private static final Path APP_PATH = Paths.get(System.getProperty("user.home"), "transactions", "budget");
    private static final Path DATA_PATH = APP_PATH.resolve("data.txt");
    private static final Path LOG_PATH = APP_PATH.resolve("log.txt");

    public static Path getDataPath() {
        return DATA_PATH;
    }

    public static Path getLogPath() {
        return LOG_PATH;
    }

    public static boolean isEmpty(Object s) {
        return s == null || s instanceof String && ((String)s).isEmpty();
    }

    public static String removeDecimalsIfDouble(String s) {
        try {
            double value = Double.parseDouble(s);
            return "" + (int) value;
        } catch (NumberFormatException e) {

            return s;
        }
    }
}
