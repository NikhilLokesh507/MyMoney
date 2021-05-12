package com.geektrust.mymoney.model.portfolio;

import com.geektrust.mymoney.model.calender.Month;
import com.geektrust.mymoney.model.market.Change;
import com.geektrust.mymoney.model.portfolio.allocation.Allocation;
import com.geektrust.mymoney.model.portfolio.statement.IncorrectBookKeeping;
import com.geektrust.mymoney.model.portfolio.statement.Record;
import org.junit.Assert;
import org.junit.Test;

public class TestRecord {

    @Test
    public void testEqualsAndHashCode() {
        Record record = Record.first(Allocation.fromString("ALLOCATE 6000 3000 1000"));
        Record newRecord = Record.ofChange(record.getAllocation(), Change.fromString("CHANGE 4.00% 10.00% 2.00%"), Month.JANUARY);
        Assert.assertEquals(record, newRecord);
    }

    @Test
    public void testOfNewMonthOnNonSuccessiveMonths() {
        Assert.assertThrows(IncorrectBookKeeping.class, () ->
                Record.ofNewMonth(Record.first(Allocation.fromString("ALLOCATE 6000 3000 1000")), null, null, Month.MARCH));
    }
}
