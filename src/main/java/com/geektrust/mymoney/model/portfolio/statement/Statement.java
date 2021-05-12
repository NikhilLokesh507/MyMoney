package com.geektrust.mymoney.model.portfolio.statement;

import com.geektrust.mymoney.model.calender.Month;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class Statement {
    List<Record> records;

    public Statement(Record record) {
        this.records = new LinkedList<>();
        records.add(record);
    }

    public void addRecord(Record record) {
        records.remove(record);
        this.records.add(record);
        this.records.sort(Comparator.comparing(Record::getMonth));
    }

    public Record getLastRecord() {
        if (records.isEmpty()) {
            throw new IncorrectBookKeeping("Statement is empty, aka in an invalid state");
        }
        return records.get(records.size() - 1);
    }

    public Record getRecordForMonth(Month month) {
        for (Record record : records) {
            if (record.getMonth().equals(month)) {
                return record;
            }
        }
        throw new NoSuchElementException();
    }
}
