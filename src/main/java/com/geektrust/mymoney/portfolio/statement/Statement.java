package com.geektrust.mymoney.portfolio.statement;

import com.geektrust.mymoney.calender.Month;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Record> getLastRecord() {
        if (records.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(records.get(records.size() - 1));
    }

    public Optional<Record> getRecordForMonth(Month month) {
        Optional<Record> optional = Optional.empty();
        for (Record record : records) {
            if (record.getMonth().equals(month)) {
                optional = Optional.of(record);
                break;
            }
        }
        return optional;
    }
}
