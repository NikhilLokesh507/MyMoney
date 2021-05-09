package com.geektrust.mymoney.portfolio.statement;

public class IncorrectBookKeeping extends RuntimeException {
    public IncorrectBookKeeping(String message) {
        super(message);
    }
}
