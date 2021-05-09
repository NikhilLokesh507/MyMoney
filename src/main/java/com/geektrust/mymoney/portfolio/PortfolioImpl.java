package com.geektrust.mymoney.portfolio;

import com.geektrust.mymoney.calender.Month;
import com.geektrust.mymoney.market.Change;
import com.geektrust.mymoney.portfolio.allocation.Allocation;
import com.geektrust.mymoney.portfolio.statement.Record;
import com.geektrust.mymoney.portfolio.statement.SIP;
import com.geektrust.mymoney.portfolio.statement.Statement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class PortfolioImpl implements Portfolio {

    String name;

    Statement statement;

    SIP sip;

    Allocation allocation;

    public PortfolioImpl(String name, SIP sip, Allocation allocation) {
        this.name = name;
        this.statement = new Statement(Record.first(allocation));
        this.sip = sip;
        this.allocation = allocation;
    }

    private Optional<Record> rebalance(Month month) {
        Optional<Record> optional = statement.getLastRecord();
        Optional<Record> rebalancedRecord = Optional.empty();
        if (optional.isPresent()) {
            Record record = optional.get();
            rebalancedRecord = Optional.of(Record.rebalanceOf(record, allocation));
        }
        return rebalancedRecord;
    }

    private boolean canRebalance() {
        boolean canRebalance = false;
        Optional<Record> optional = statement.getLastRecord();
        if (optional.isPresent()) {
            canRebalance = optional.get().getMonth().canRebalance();
        }
        return canRebalance;
    }

    @Override
    public void onEventReceived(Month month, Change change) {
        Optional<Record> optional = statement.getLastRecord();
        if (month == Month.JANUARY) {
            optional.ifPresent(record ->
                    statement.addRecord(Record.ofChange(record.getAllocation(), change, month)));
        } else {
            optional.ifPresent(record ->
                    statement.addRecord(Record.ofNewMonth(record, sip, change, month)));
        }
        if (month == Month.JUNE || month == Month.DECEMBER) {
            rebalance(month).ifPresent(statement::addRecord);
        }

    }

    @Override
    public void printBalance(Month month) {
        Optional<Record> recordOptional = statement.getRecordForMonth(month);
        recordOptional.ifPresent(record -> System.out.println(record.getAllocation()));
    }

    @Override
    public String rebalancedBalance() {
        String result = "CANNOT_REBALANCE";
        if(canRebalance()) {
            Optional<Record> record = statement.getLastRecord();
            if (record.isPresent()) {
                Month lastRebalancedMonth = !record.get().getMonth().equals(Month.DECEMBER) ? Month.JUNE : Month.DECEMBER;
                Optional<Record> rebalancedRecord = statement.getRecordForMonth(lastRebalancedMonth);
                if (rebalancedRecord.isPresent()) {
                    result = rebalancedRecord.get().getAllocation().toString();
                }
            }
        }
        return result;
    }


}
