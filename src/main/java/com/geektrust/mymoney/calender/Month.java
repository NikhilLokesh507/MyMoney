package com.geektrust.mymoney.calender;

import java.util.NoSuchElementException;

public enum Month {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    private final int month;

    Month(int month) {
        this.month = month;
    }

    public int month() {
        return this.month;
    }

    public boolean canRebalance() {
        return this.month >= 6;
    }

    public static Month fromInt(int month) {
        for (Month month1 : values()) {
            if (month1.month() == month) {
                return month1;
            }
        }
        throw new NoSuchElementException("No month with index " + month);
    }

    public boolean isAfter(Month month) {
        return this.month == month.month() + 1;
    }

}
