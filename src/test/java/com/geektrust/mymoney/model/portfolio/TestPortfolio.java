package com.geektrust.mymoney.model.portfolio;

import com.geektrust.mymoney.model.calender.Month;
import com.geektrust.mymoney.model.market.Change;
import com.geektrust.mymoney.model.market.Market;
import com.geektrust.mymoney.model.portfolio.allocation.Allocation;
import com.geektrust.mymoney.model.portfolio.statement.SIP;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.sound.sampled.Port;

public class TestPortfolio {

    private Portfolio portfolio;

    private Market market;

    @Before
    public void setup() {
        market = Mockito.mock(Market.class);
        portfolio = new PortfolioImpl(Allocation.fromString("ALLOCATE 6000 3000 1000"));
        Mockito.doAnswer(((Answer<Void>) invocationOnMock -> {
            portfolio.onEventReceived(invocationOnMock.getArgumentAt(0, Month.class), invocationOnMock.getArgumentAt(1, Change.class));
            return null;
        })).when(market).onEventGenerated(Mockito.any(Month.class), Mockito.any(Change.class));
        Mockito.doAnswer(((Answer<Portfolio>) invocationOnMock -> portfolio)).when(market).getPortfolioById(Mockito.anyString());
        portfolio.setSip(SIP.fromString("SIP 2000 1000 500"));

        market.addSubscriber(portfolio);
    }

    @After
    public void tearDown() {
        market.purgeSubscribers();
    }

    @Test
    public void testRebalance() {
        market.onEventGenerated(Month.JANUARY, Change.fromString("CHANGE 4.00% 10.00% 2.00%"));
        market.onEventGenerated(Month.FEBRUARY, Change.fromString("CHANGE -10.00% 40.00% 0.00%"));
        market.onEventGenerated(Month.MARCH, Change.fromString("CHANGE 12.50% 12.50% 12.50%"));
        Assert.assertEquals("CANNOT_REBALANCE", market.getPortfolioById(portfolio.getId()).rebalancedBalance());
        market.onEventGenerated(Month.APRIL, Change.fromString("CHANGE 8.00% -3.00% 7.00%"));
        market.onEventGenerated(Month.MAY, Change.fromString("CHANGE 13.00% 21.00% 10.50%"));
        market.onEventGenerated(Month.JUNE, Change.fromString("CHANGE 10.00% 8.00% -5.00%"));
        Assert.assertEquals("23619 11809 3936", market.getPortfolioById(portfolio.getId()).rebalancedBalance());
    }
}
