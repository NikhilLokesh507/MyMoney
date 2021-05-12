package com.geektrust.mymoney.model.portfolio.statement;

import com.geektrust.mymoney.model.calender.Month;
import com.geektrust.mymoney.model.market.Change;
import com.geektrust.mymoney.model.portfolio.allocation.Allocation;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(of = {"month"})
@Getter
public class Record {

    Month month;

    Allocation allocation;

    private Record(Month month, Allocation allocation) {
        if (Objects.isNull(month) || Objects.isNull(allocation)) {
            throw new IllegalArgumentException();
        }
        this.month = month;
        this.allocation = allocation;
    }

    @Override
    public String toString() {
        return month + " " + allocation;
    }

    public static Record ofNewMonth(Record record, SIP sip, Change change, Month month) {
        if (!month.isAfter(record.getMonth())) {
            throw new IncorrectBookKeeping(String.format("%s is not after %s", month, record.getMonth()));
        }
        Allocation afterSip = Allocation.postSip(record.getAllocation(), sip);
        Allocation allocation = Allocation.postChange(afterSip, change);
        return new Record(month, allocation);
    }

    public static Record rebalanceOf(Record record, Allocation allocation) {
        Allocation newAllocation = Allocation.rebalanceOf(record.getAllocation(), allocation);
        return new Record(record.getMonth(), newAllocation);
    }

    public static Record first(Allocation previousAllocation) {
        return new Record(Month.JANUARY, previousAllocation);
    }

    public static Record ofChange(Allocation previousAllocation, Change change, Month month) {
        Allocation afterChange = Allocation.postChange(previousAllocation, change);
        return new Record(month, afterChange);
    }
}
