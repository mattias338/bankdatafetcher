package com.banken.personalbudget;

import com.banken.personalbudget.data.Data;

public interface Storage {
    void storeData(Data data);

    Data getData();
}
