package com.geektrust.mymoney.market;

import com.geektrust.mymoney.calender.Month;

public interface MonthlyEventGenerator {
    void onEventGenerated(Month month, Change change);
}
