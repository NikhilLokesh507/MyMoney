package com.geektrust.mymoney.model.market;

import com.geektrust.mymoney.model.calender.Month;
import com.geektrust.mymoney.model.portfolio.Portfolio;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Market implements MonthlyEventGenerator {

    private List<Portfolio> subscribers;

    private Market() {
        subscribers = new LinkedList<>();
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

    public void addSubscriber(Portfolio portfolio) {
        subscribers.add(portfolio);
    }

    public void purgeSubscribers() {
        subscribers = new LinkedList<>();
    }

    public Portfolio getPortfolioById(String id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException();
        }
        for (Portfolio portfolio : subscribers) {
            if (portfolio.getId().equals(id)) {
                return portfolio;
            }
        }
        throw new NoSuchElementException();
    }
}
