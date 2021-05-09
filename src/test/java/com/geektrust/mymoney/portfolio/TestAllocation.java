package com.geektrust.mymoney.portfolio;

import com.geektrust.mymoney.market.Change;
import com.geektrust.mymoney.portfolio.allocation.Allocation;
import com.geektrust.mymoney.portfolio.statement.SIP;
import org.junit.Assert;
import org.junit.Test;

public class TestAllocation {

    @Test
    public void testConstructorPostSip() {
        Allocation allocation = Allocation.fromString("ALLOCATE 6240 3300 1020");
        SIP sip = SIP.builder().string("SIP 2000 1000 500").build();
        Assert.assertEquals(Allocation.fromString("ALLOCATE 8240 4300 1520"), Allocation.postSip(allocation, sip));
    }

    @Test
    public void testConstructorPostChange() {
        Allocation allocation = Allocation.fromString("ALLOCATE 8240 4300 1520");
        Change change = Change.fromString("CHANGE -10.00% 40.00% 0.00%");
        Assert.assertEquals(Allocation.fromString("ALLOCATE 7416 6020 1520"), Allocation.postChange(allocation, change));
    }

    @Test
    public void testConstructorRebalanceOf() {
        Allocation rebalancedAllocation = Allocation.fromString("ALLOCATE 6000 3000 1000");
        Allocation allocation = Allocation.fromString("ALLOCATE 21590 13664 4112");
        Assert.assertEquals(Allocation.fromString("ALLOCATE 23619 11809 3936"), Allocation.rebalanceOf(allocation, rebalancedAllocation));
    }
}
