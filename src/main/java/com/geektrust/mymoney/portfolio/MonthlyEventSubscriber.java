package com.geektrust.mymoney.portfolio;

import com.geektrust.mymoney.calender.Month;
import com.geektrust.mymoney.market.Change;

public interface MonthlyEventSubscriber {
    void onEventReceived(Month month, Change change);

    void printBalance(Month month);

    void printRebalancedBalance();
}
