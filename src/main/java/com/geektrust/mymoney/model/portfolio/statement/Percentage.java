package com.geektrust.mymoney.model.portfolio.statement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.NoSuchElementException;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Percentage {
    Double value;

    @Builder
    public static Percentage of(Double percentage) {
        if (percentage < -100 || percentage > 100 || percentage.isInfinite() || percentage.isNaN()) {
            throw new NoSuchElementException("Not a valid percentage value");
        }
        return new Percentage(percentage);
    }
}
