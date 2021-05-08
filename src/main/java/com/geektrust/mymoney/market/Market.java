package com.geektrust.mymoney.market;

import com.geektrust.mymoney.calender.Month;
import com.geektrust.mymoney.portfolio.Portfolio;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class Market implements MonthlyEventGenerator {

    List<Portfolio> subscribers;

    private Market() {
    }

    private static Market instance;

    public static Market getInstance() {
        if (Objects.isNull(instance)) {
            instance = new Market();
        }
        return instance;
    }

    @Override
    public void onEventGenerated(Month month, Change change) {
        subscribers.forEach(subscriber -> subscriber.onEventReceived(month, change));
    }
}
