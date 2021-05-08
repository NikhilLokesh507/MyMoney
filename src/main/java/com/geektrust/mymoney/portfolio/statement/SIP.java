package com.geektrust.mymoney.portfolio.statement;

import com.geektrust.mymoney.portfolio.Asset;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SIP {
    Map<Asset, Long> sip;

    @Builder
    public static SIP fromString(String string) {
        String[] strings = string.split(" ");
        if (!strings[0].equals("SIP")) {
            throw new NoSuchElementException();
        }
        Long equity = Long.parseLong(strings[1]);
        Long debt = Long.parseLong(strings[2]);
        Long gold = Long.parseLong(strings[3]);
        Map<Asset, Long> sip = new HashMap<>();
        sip.put(Asset.EQUITY, equity);
        sip.put(Asset.DEBT, debt);
        sip.put(Asset.GOLD, gold);
        return new SIP(sip);
    }
}
