package com.banken.personalbudget;

public class WrapAroundInt {
    private int value = 0;
    private int max;

    public void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("Bad value: " + max);
        }
        this.max = max;
    }

    public int getValue() {
        return value;
    }

    public void inc() {
        value++;
        if (value > max) {
            value = 0;
        }
    }

    public void dec() {
        value--;
        if (value == -1) {
            value = max;
        }
    }
}
