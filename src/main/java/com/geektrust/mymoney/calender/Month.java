package com.geektrust.mymoney.calender;

public enum Month {
    JANUARY(false, 1),
    FEBRUARY(false, 2),
    MARCH(false, 3),
    APRIL(false, 4),
    MAY(false, 5),
    JUNE(true, 6),
    JULY(false, 7),
    AUGUST(false, 8),
    SEPTEMBER(false, 9),
    OCTOBER(false, 10),
    NOVEMBER(false, 11),
    DECEMBER(true, 12);

    private final boolean canRebalance;

    private final int month;

    Month(boolean canRebalance, int month) {
        this.canRebalance = canRebalance;
        this.month = month;
    }

    public boolean canRebalance() {
        return canRebalance;
    }
}
