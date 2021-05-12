package com.geektrust.mymoney.model.portfolio.statement;

public class IncorrectBookKeeping extends RuntimeException {
    public IncorrectBookKeeping(String message) {
        super(message);
    }
}
