package com.geektrust.mymoney.exchange;

public class CTA {

    private static final String ALLOCATE = "ALLOCATE";
    private static final String SIP = "SIP";
    private static final String CHANGE = "CHANGE";
    private static final String BALANCE = "BALANCE";
    private static final String REBALANCE = "REBALANCE";


    public CTA() {
    }

    public void process(String command) {
        String[] strings = command.split(" ");
        String action = strings[0];
        switch (action) {
            case ALLOCATE:

        }
    }
}
