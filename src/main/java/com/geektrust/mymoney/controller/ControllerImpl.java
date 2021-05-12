package com.geektrust.mymoney.controller;

import com.geektrust.mymoney.model.calender.Month;
import com.geektrust.mymoney.model.market.Change;
import com.geektrust.mymoney.model.market.Market;
import com.geektrust.mymoney.model.portfolio.Portfolio;
import com.geektrust.mymoney.model.portfolio.PortfolioFactory;
import com.geektrust.mymoney.model.portfolio.allocation.Allocation;
import com.geektrust.mymoney.model.portfolio.statement.SIP;

import java.io.IOException;

public class ControllerImpl implements Controller {

    private final Market market;

    private final PortfolioFactory portfolioFactory;

    public ControllerImpl() {
        this.market = Market.getInstance();
        this.portfolioFactory = PortfolioFactory.instance();
    }

    @Override
    public String allocate(Allocation allocation) {
        Portfolio portfolio = portfolioFactory.newPortfolio(allocation);
        market.addSubscriber(portfolio);
        return portfolio.getId();
    }

    @Override
    public void registerSip(String portfolioId, SIP sip) {
        market.getPortfolioById(portfolioId).setSip(sip);
    }

    @Override
    public void markChange(Month month, Change change) {
        market.onEventGenerated(month, change);
    }

    @Override
    public void printBalance(String portfolioId, Month month) {
        System.out.println(market.getPortfolioById(portfolioId).balance(month));
    }

    @Override
    public void printRebalancedAllocation(String portfolioId) {
        System.out.println(market.getPortfolioById(portfolioId).rebalancedBalance());
    }

    @Override
    public void close() throws IOException {
        market.purgeSubscribers();
    }
}
