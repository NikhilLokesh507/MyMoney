package com.geektrust.mymoney;

import com.geektrust.mymoney.calender.Month;
import com.geektrust.mymoney.market.Change;
import com.geektrust.mymoney.market.Market;
import com.geektrust.mymoney.portfolio.MonthlyEventSubscriber;
import com.geektrust.mymoney.portfolio.PortfolioImpl;
import com.geektrust.mymoney.portfolio.allocation.Allocation;
import com.geektrust.mymoney.portfolio.statement.SIP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Allocation allocation = null;
        SIP sip = null;
        try (Market market = Market.getInstance()) {
            for (String string : commands(args[0])) {
                String[] strings = string.split(" ");
                switch (strings[0]) {
                    case "ALLOCATE":
                        allocation = Allocation.fromString(string);
                        break;
                    case "SIP":
                        sip = SIP.fromString(string);
                        market.setSubscribers(Collections.singletonList(new PortfolioImpl("main", sip, allocation)));
                        break;
                    case "CHANGE":
                        market.onEventGenerated(Month.valueOf(strings[4]), Change.fromString(string));
                        break;
                    case "BALANCE":
                        market.getSubscribers().forEach(portfolio -> portfolio.printBalance(Month.valueOf(strings[1])));
                        break;
                    case "REBALANCE":
                        market.getSubscribers().forEach(subscriber -> System.out.println(subscriber.rebalancedBalance()));
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<String> commands(String file) throws IOException {
        List<String> list = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }
}
