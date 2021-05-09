package com.geektrust.mymoney.portfolio.allocation;

import com.geektrust.mymoney.market.Change;
import com.geektrust.mymoney.portfolio.Asset;
import com.geektrust.mymoney.portfolio.statement.Percentage;
import com.geektrust.mymoney.portfolio.statement.SIP;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.*;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Allocation {

    Map<Asset, Double> assetDistribution;

    @Getter
    Double total;

    public Map<Asset, Double> getAllocation() {
        return this.assetDistribution;
    }

    private Allocation(Map<Asset, Double> assetDistribution) {
        Double total1 = 0d;
        for (Map.Entry<Asset, Double> entry : assetDistribution.entrySet()) {
            total1 += entry.getValue();
        }
        this.assetDistribution = assetDistribution;
        this.total = total1;
    }

    @Override
    public String toString() {
        StringBuilder value = new StringBuilder();
        List<Asset> list = Arrays.asList(Asset.EQUITY, Asset.DEBT, Asset.GOLD);
        for (Asset asset : list) {
            value.append(Math.round(assetDistribution.get(asset))).append(" ");
        }
        return value.toString().trim();
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
        Map<Asset, Double> assetDistributionMap = new EnumMap<>(Asset.class);
        assetDistributionMap.put(Asset.EQUITY, equity);
        assetDistributionMap.put(Asset.DEBT, debt);
        assetDistributionMap.put(Asset.GOLD, gold);
        return new Allocation(assetDistributionMap, total);
    }

    @Builder
    public static Allocation postSip(Allocation allocation, SIP sip) {
        Map<Asset, Double> assetDoubleMap = new EnumMap<>(allocation.getAllocation());
        Map<Asset, Double> sipMap = new EnumMap<>(sip.getSip());
        for (Map.Entry<Asset, Double> entry : sipMap.entrySet()) {
            double sipIncrement = entry.getValue();
            if (assetDoubleMap.containsKey(entry.getKey())) {
                double alloc = assetDoubleMap.get(entry.getKey());
                assetDoubleMap.put(entry.getKey(), alloc + sipIncrement);
            }
        }
        return new Allocation(assetDoubleMap);
    }

    @Builder
    public static Allocation postChange(Allocation allocation, Change change) {
        Map<Asset, Double> assetDoubleMap = new EnumMap<>(allocation.getAllocation());
        Map<Asset, Percentage> changePercentage = new EnumMap<>(change.getChange());
        for (Map.Entry<Asset, Double> entry : assetDoubleMap.entrySet()) {
            Asset asset = entry.getKey();
            double alloc = entry.getValue();
            Percentage percentage = changePercentage.get(asset);
            double value = Math.floor(alloc * (1d + percentage.getValue() / 100d));
            assetDoubleMap.put(asset, value);
        }
        return new Allocation(assetDoubleMap);
    }

    @Builder
    public static Allocation rebalanceOf(Allocation allocation, Allocation rebalancedAllocation) {
        double total = allocation.getTotal();
        Map<Asset, Double> allocationMap = new EnumMap<>(allocation.getAllocation());
        Map<Asset, Double> rMap = new EnumMap<>(rebalancedAllocation.getAllocation());
        for (Map.Entry<Asset, Double> entry : rMap.entrySet()) {
            double value = ((double) entry.getValue() / rebalancedAllocation.getTotal()) * total;
            allocationMap.put(entry.getKey(), Math.floor(value));
        }
        return new Allocation(allocationMap);
    }
}
