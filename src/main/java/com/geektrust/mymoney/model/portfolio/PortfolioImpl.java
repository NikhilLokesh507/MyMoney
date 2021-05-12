package com.geektrust.mymoney.model.portfolio;

import com.geektrust.mymoney.model.calender.Month;
import com.geektrust.mymoney.model.market.Change;
import com.geektrust.mymoney.model.portfolio.allocation.Allocation;
import com.geektrust.mymoney.model.portfolio.exceptions.SIPModificationAttempt;
import com.geektrust.mymoney.model.portfolio.statement.Record;
import com.geektrust.mymoney.model.portfolio.statement.SIP;
import com.geektrust.mymoney.model.portfolio.statement.Statement;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(of = {"id"})
public class PortfolioImpl implements Portfolio {

    final String id;

    final Statement statement;

    SIP sip;

    final Allocation allocation;

    boolean rebalanced;

    private static final AtomicInteger counter = new AtomicInteger(0);

    public PortfolioImpl(Allocation allocation) {

        this.id = Integer.toString(counter.incrementAndGet());
        this.statement = new Statement(Record.first(allocation));
        this.allocation = allocation;
        rebalanced = false;
    }

    @Override
    public void setSip(SIP sip) {
        if (Objects.isNull(this.sip)) {
            this.sip = sip;
        } else {
            throw new SIPModificationAttempt();
        }
    }

    private Record rebalance() {
        Record record = statement.getLastRecord();
        rebalanced = true;
        return Record.rebalanceOf(record, allocation);
    }

    @Override
    public void onEventReceived(Month month, Change change) {
        Record record = statement.getLastRecord();
        if (month == Month.JANUARY) {
            statement.addRecord(Record.ofChange(record.getAllocation(), change, month));
        } else {
            statement.addRecord(Record.ofNewMonth(record, sip, change, month));
        }
        if (month == Month.JUNE || month == Month.DECEMBER) {
            statement.addRecord(rebalance());
        }

    }

    @Override
    public String balance(Month month) {
        Record record = statement.getRecordForMonth(month);
        return record.getAllocation().toString();
    }

    @Override
    public String rebalancedBalance() {
        String result = "CANNOT_REBALANCE";
        if (rebalanced) {
            Record record = statement.getLastRecord();
            Month lastRebalancedMonth = !record.getMonth().equals(Month.DECEMBER) ? Month.JUNE : Month.DECEMBER;
            Record rebalancedRecord = statement.getRecordForMonth(lastRebalancedMonth);
            result = rebalancedRecord.getAllocation().toString();
        }
        return result;
    }


}
