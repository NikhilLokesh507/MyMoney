package com.geektrust.mymoney.portfolio.statement;

import com.geektrust.mymoney.calender.Month;
import com.geektrust.mymoney.portfolio.allocation.Allocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode(of = {"month"})
@Getter
@AllArgsConstructor
public class Record {
    Month month;
    Allocation allocation;
}
