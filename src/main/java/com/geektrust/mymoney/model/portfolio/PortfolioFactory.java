package com.geektrust.mymoney.model.portfolio;

import com.geektrust.mymoney.model.portfolio.allocation.Allocation;

import java.util.Objects;

public class PortfolioFactory {

    private static PortfolioFactory portfolioFactory;

    private PortfolioFactory() {
    }

    public static PortfolioFactory instance() {

        if (Objects.isNull(portfolioFactory)) {
            portfolioFactory = new PortfolioFactory();
        }
        return portfolioFactory;

    }

    public Portfolio newPortfolio(Allocation allocation) {
        return new PortfolioImpl(allocation);
    }
}
