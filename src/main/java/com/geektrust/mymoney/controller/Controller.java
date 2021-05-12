package com.geektrust.mymoney.controller;

import com.geektrust.mymoney.model.calender.Month;
import com.geektrust.mymoney.model.market.Change;
import com.geektrust.mymoney.model.portfolio.allocation.Allocation;
import com.geektrust.mymoney.model.portfolio.statement.SIP;

import java.io.Closeable;

public interface Controller extends Closeable {

    String allocate(Allocation allocation);

    void registerSip(String portfolioId, SIP sip);

    void markChange(Month month, Change change);

    void printBalance(String portfolioId, Month month);

    void printRebalancedAllocation(String portfolioId);

}
