package com.geektrust.mymoney.portfolio;

import com.geektrust.mymoney.portfolio.allocation.Allocation;

import java.util.Optional;

public interface Rebalancer {
    Optional<Allocation> rebalance();
}
