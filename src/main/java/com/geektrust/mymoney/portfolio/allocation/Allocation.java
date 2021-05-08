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

    Map<Asset, Double> assetDistribution;

    @Getter
    Double total;

    public Map<Asset, Double> getAllocation() {
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
    public static Allocation of(Map<Asset, Double> assetDistribution) {
        Double total = 0D;
        for (Map.Entry<Asset, Double> entry : assetDistribution.entrySet()) {
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
        Double equity = Double.parseDouble(strings[1]);
        Double debt = Double.parseDouble(strings[2]);
        Double gold = Double.parseDouble(strings[3]);
        Double total = equity + debt + gold;
        Map<Asset, Double> assetDistributionMap = new HashMap<>();
        assetDistributionMap.put(Asset.EQUITY, equity);
        assetDistributionMap.put(Asset.DEBT, debt);
        assetDistributionMap.put(Asset.GOLD, gold);
        return new Allocation(assetDistributionMap, total);
    }

    @Builder
    public static Allocation monthlyChangeOf(Allocation allocation, SIP sip, Change change, Month month) {
        Map<Asset, Double> assetDoubleMap = allocation.getAllocation();
        Map<Asset, Double> sipMap = sip.getSip();
        Map<Asset, Percentage> changeMap = change.getChange();
        Map<Asset, Double> newAllocation = new HashMap<>(assetDoubleMap);
        Double total = 0D;

        if (!month.equals(Month.JANUARY)) {
            for (Map.Entry<Asset, Double> entry : sipMap.entrySet()) {
                if (assetDoubleMap.containsKey(entry.getKey())) {
                    double value = assetDoubleMap.get(entry.getKey()) + entry.getValue();
                    newAllocation.put(entry.getKey(), (double) Math.round(value));
                } else {
                    newAllocation.put(entry.getKey(), entry.getValue());
                }
            }
        }


        for (Map.Entry<Asset, Double> entry : newAllocation.entrySet()) {
            Asset asset = entry.getKey();
            double value = entry.getValue() * (1 + changeMap.get(asset).getValue() / 100D);
            double roundValue = (double) Math.round(value);
            total += roundValue;
            newAllocation.put(asset, roundValue);
        }
        Allocation allocation1 = new Allocation(newAllocation, total);
        System.out.println(month + " " + allocation1);
        return allocation1;
    }

    @Builder
    public static Allocation rebalanceOf(Allocation allocation, Allocation rebalancedAllocation) {
        Double total = allocation.getTotal();
        Map<Asset, Double> allocationMap = new HashMap<>();
        Map<Asset, Double> rMap = new HashMap<>(rebalancedAllocation.getAllocation());
        for (Map.Entry<Asset, Double> entry : rMap.entrySet()) {
            double value = ((double) entry.getValue() / rebalancedAllocation.getTotal()) * total;
            allocationMap.put(entry.getKey(), (double) Math.round(value));
        }
        return Allocation.builder().assetDistribution(allocationMap).build();
    }
}
