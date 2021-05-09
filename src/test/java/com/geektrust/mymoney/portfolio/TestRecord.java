package com.geektrust.mymoney.portfolio;

import com.geektrust.mymoney.calender.Month;
import com.geektrust.mymoney.market.Change;
import com.geektrust.mymoney.portfolio.allocation.Allocation;
import com.geektrust.mymoney.portfolio.statement.Record;
import org.junit.Assert;
import org.junit.Test;

public class TestRecord {

    @Test
    public void testEqualsAndHashCode() {
        Record record = Record.first(Allocation.fromString("ALLOCATE 6000 3000 1000"));
        Record newRecord = Record.ofChange(record.getAllocation(), Change.fromString("CHANGE 4.00% 10.00% 2.00%"), Month.JANUARY);
        Assert.assertTrue(record.equals(newRecord));
    }
}
