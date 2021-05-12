package com.geektrust.mymoney.model.market;

import com.geektrust.mymoney.model.calender.Month;

public interface MonthlyEventGenerator {
    void onEventGenerated(Month month, Change change);
}
