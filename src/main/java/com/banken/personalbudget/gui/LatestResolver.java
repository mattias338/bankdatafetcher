package com.banken.personalbudget.gui;

public interface LatestResolver<T> {
    T resolve(String otherParty);
}
