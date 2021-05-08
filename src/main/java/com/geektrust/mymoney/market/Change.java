package com.geektrust.mymoney.market;

import com.geektrust.mymoney.portfolio.Asset;
import com.geektrust.mymoney.portfolio.statement.Percentage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Change {

    Map<Asset, Percentage> update;

    public Map<Asset, Percentage> getChange() {
        return this.update;
    }

    @Builder
    public static Change of(Map<Asset, Percentage> update) {
        return new Change(update);
    }

    @Builder
    public static Change fromString(String string) {
        String[] strings = string.split(" ");
        if (!strings[0].equals("CHANGE")) {
            throw new NoSuchElementException();
        }
        Double equity = Double.parseDouble(strings[1].substring(0, strings[1].length() - 1));
        Double debt = Double.parseDouble(strings[2].substring(0, strings[2].length() - 1));
        Double gold = Double.parseDouble(strings[3].substring(0, strings[3].length() - 1));
        Map<Asset, Percentage> update = new HashMap<>();
        update.put(Asset.DEBT, Percentage.of(debt));
        update.put(Asset.GOLD, Percentage.of(gold));
        update.put(Asset.EQUITY, Percentage.of(equity));
        return new Change(update);
    }
}
