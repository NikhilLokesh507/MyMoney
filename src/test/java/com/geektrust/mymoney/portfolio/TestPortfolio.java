package com.geektrust.mymoney.portfolio;

import com.geektrust.mymoney.calender.Month;
import com.geektrust.mymoney.market.Change;
import com.geektrust.mymoney.market.Market;
import com.geektrust.mymoney.portfolio.allocation.Allocation;
import com.geektrust.mymoney.portfolio.statement.SIP;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class TestPortfolio {

    private Portfolio portfolio;

    private Market market;

    @Before
    public void setup() {
        portfolio = new PortfolioImpl("test", SIP.fromString("SIP 2000 1000 500"), Allocation.fromString("ALLOCATE 6000 3000 1000"));
        market = Market.getInstance();
        market.setSubscribers(Collections.singletonList(portfolio));
    }

    @After
    public void tearDown() {
        portfolio = null;
        market.close();
    }

    @Test
    public void cannotRebalanceWithoutSufficientData() {
        market.onEventGenerated(Month.JANUARY, Change.fromString("CHANGE 4.00% 10.00% 2.00%"));
        market.onEventGenerated(Month.FEBRUARY, Change.fromString("CHANGE -10.00% 40.00% 0.00%"));
        market.onEventGenerated(Month.MARCH, Change.fromString("CHANGE 12.50% 12.50% 12.50%"));
        Assert.assertEquals("CANNOT_REBALANCE", portfolio.rebalancedBalance());
    }

    @Test
    public void testPortfolioRebalancing() {
        market.onEventGenerated(Month.JANUARY, Change.fromString("CHANGE 4.00% 10.00% 2.00%"));
        market.onEventGenerated(Month.FEBRUARY, Change.fromString("CHANGE -10.00% 40.00% 0.00%"));
        market.onEventGenerated(Month.MARCH, Change.fromString("CHANGE 12.50% 12.50% 12.50%"));
        market.onEventGenerated(Month.APRIL, Change.fromString("CHANGE 8.00% -3.00% 7.00%"));
        market.onEventGenerated(Month.MAY, Change.fromString("CHANGE 13.00% 21.00% 10.50%"));
        market.onEventGenerated(Month.JUNE, Change.fromString("CHANGE 10.00% 8.00% -5.00%"));
        Assert.assertEquals("23619 11809 3936", portfolio.rebalancedBalance());
    }
}
