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
        this.statement = new Statement(new Record(Month.JANUARY, allocation));
        this.sip = sip;
        this.allocation = allocation;
    }

    @Override
    public Optional<Allocation> rebalance() {
        Optional<Record> optional = statement.getLastRecord();
        Optional<Allocation> optionalAllocation = Optional.empty();
        if (optional.isPresent() && canRebalance()) {
            Record record = optional.get();
            Allocation rebalancedAllocation = Allocation.rebalanceOf(record.getAllocation(), this.allocation);
            statement.addRecord(new Record(record.getMonth(), rebalancedAllocation));
            optionalAllocation = Optional.ofNullable(rebalancedAllocation);
        }
        return optionalAllocation;
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
        optional.ifPresent(record ->
                statement.addRecord(new Record(month, Allocation.monthlyChangeOf(record.getAllocation(), sip, change, month))));
    }

    @Override
    public void printBalance(Month month) {
        Optional<Record> recordOptional = statement.getRecordForMonth(month);
        recordOptional.ifPresent(record -> System.out.println(record.getAllocation()));
    }
}
