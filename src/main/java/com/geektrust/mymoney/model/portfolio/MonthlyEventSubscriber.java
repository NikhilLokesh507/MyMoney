package com.geektrust.mymoney.model.portfolio;

import com.geektrust.mymoney.model.calender.Month;
import com.geektrust.mymoney.model.market.Change;

public interface MonthlyEventSubscriber {
    void onEventReceived(Month month, Change change);

    String balance(Month month);

    String rebalancedBalance();
}
