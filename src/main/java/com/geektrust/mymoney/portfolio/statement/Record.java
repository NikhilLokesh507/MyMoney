package com.geektrust.mymoney.portfolio.statement;

import com.geektrust.mymoney.calender.Month;
import com.geektrust.mymoney.market.Change;
import com.geektrust.mymoney.portfolio.allocation.Allocation;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(of = {"month"})
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Record {
    Month month;
    Allocation allocation;
    Allocation incomingAllocation;
    Allocation afterSip;

    private Record(Month month, Allocation allocation) {
        this.month = month;
        this.allocation = allocation;
        this.incomingAllocation = null;
        this.afterSip = null;
    }

    @Override
    public String toString() {
        return month + " " + allocation;
    }

    @Builder
    public static Record ofNewMonth(Record record, SIP sip, Change change, Month month) {
        if (!month.isAfter(record.getMonth())) {
            throw new IncorrectBookKeeping(String.format("%s is not after %s", month, record.getMonth()));
        }
        Allocation afterSip = Allocation.postSip(record.getAllocation(), sip);
        Allocation allocation = Allocation.postChange(afterSip, change);
        return new Record(month, allocation, record.getIncomingAllocation(), afterSip);
    }

    @Builder
    public static Record rebalanceOf(Record record, Allocation allocation) {
        Allocation newAllocation = Allocation.rebalanceOf(record.getAllocation(), allocation);
        return new Record(record.getMonth(), newAllocation, record.getIncomingAllocation(), record.getAfterSip());
    }

    @Builder
    public static Record first(Allocation previousAllocation) {
        return new Record(Month.JANUARY, previousAllocation);
    }

    @Builder
    public static Record ofChange(Allocation previousAllocation, Change change, Month month) {
        Allocation afterChange = Allocation.postChange(previousAllocation, change);
        return new Record(month, afterChange, previousAllocation, null);
    }
}
