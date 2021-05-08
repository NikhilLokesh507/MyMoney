package com.geektrust.mymoney.portfolio.allocation;

import com.geektrust.mymoney.calender.Month;
import com.geektrust.mymoney.market.Change;
import com.geektrust.mymoney.portfolio.Asset;
import com.geektrust.mymoney.portfolio.statement.Percentage;
import com.geektrust.mymoney.portfolio.statement.SIP;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.*;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Allocation {

    Map<Asset, Long> assetDistribution;

    @Getter
    Long total;

    public Map<Asset, Long> getAllocation() {
        return this.assetDistribution;
    }

    @Override
    public String toString() {
        StringBuilder value = new StringBuilder();
        List<Asset> list = Arrays.asList(Asset.EQUITY, Asset.DEBT, Asset.GOLD);
        for (Asset asset : list) {
            value.append(Math.round(assetDistribution.get(asset))).append(" ");
        }
        return value.toString();
    }

    @Builder
    public static Allocation of(Map<Asset, Long> assetDistribution) {
        Long total = 0L;
        for (Map.Entry<Asset, Long> entry : assetDistribution.entrySet()) {
            total += entry.getValue();
        }
        return new Allocation(assetDistribution, total);
    }

    @Builder
    public static Allocation fromString(String string) {
        String[] strings = string.split(" ");
        if (!strings[0].equals("ALLOCATE")) {
            throw new NoSuchElementException();
        }
        Long equity = Long.parseLong(strings[1]);
        Long debt = Long.parseLong(strings[2]);
        Long gold = Long.parseLong(strings[3]);
        Long total = equity + debt + gold;
        Map<Asset, Long> assetDistributionMap = new HashMap<>();
        assetDistributionMap.put(Asset.EQUITY, equity);
        assetDistributionMap.put(Asset.DEBT, debt);
        assetDistributionMap.put(Asset.GOLD, gold);
        return new Allocation(assetDistributionMap, total);
    }

    @Builder
    public static Allocation monthlyChangeOf(Allocation allocation, SIP sip, Change change, Month month) {
        Map<Asset, Long> assetDoubleMap = allocation.getAllocation();
        Map<Asset, Long> sipMap = sip.getSip();
        Map<Asset, Percentage> changeMap = change.getChange();
        Map<Asset, Long> newAllocation = new HashMap<>(assetDoubleMap);
        Long total = 0L;

        if (!month.equals(Month.JANUARY)) {
            for (Map.Entry<Asset, Long> entry : sipMap.entrySet()) {
                if (assetDoubleMap.containsKey(entry.getKey())) {
                    long value = assetDoubleMap.get(entry.getKey()) + entry.getValue();
                    newAllocation.put(entry.getKey(), value);
                } else {
                    newAllocation.put(entry.getKey(), entry.getValue());
                }
            }
        }


        for (Map.Entry<Asset, Long> entry : newAllocation.entrySet()) {
            Asset asset = entry.getKey();
            double value = entry.getValue() * (1 + changeMap.get(asset).getValue() / 100D);
            long roundValue = Math.round(value);
            total += roundValue;
            newAllocation.put(asset, roundValue);
        }
        Allocation allocation1 = new Allocation(newAllocation, total);
        System.out.println(month + " " + allocation1);
        return allocation1;
    }

    @Builder
    public static Allocation rebalanceOf(Allocation allocation, Allocation rebalancedAllocation) {
        Long total = allocation.getTotal();
        Map<Asset, Long> allocationMap = new HashMap<>();
        Map<Asset, Long> rMap = new HashMap<>(rebalancedAllocation.getAllocation());
        for (Map.Entry<Asset, Long> entry : rMap.entrySet()) {
            double value = ((double) entry.getValue() / rebalancedAllocation.getTotal()) * total;
            allocationMap.put(entry.getKey(), Math.round(value));
        }
        return Allocation.builder().assetDistribution(allocationMap).build();
    }
}
