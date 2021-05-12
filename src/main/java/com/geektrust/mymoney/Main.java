package com.geektrust.mymoney;

import com.geektrust.mymoney.controller.Controller;
import com.geektrust.mymoney.controller.ControllerFactory;
import com.geektrust.mymoney.model.calender.Month;
import com.geektrust.mymoney.model.market.Change;
import com.geektrust.mymoney.model.portfolio.allocation.Allocation;
import com.geektrust.mymoney.model.portfolio.statement.SIP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            String id = null;
            Controller controller = ControllerFactory.instance().controller();
            for (String string : commands(args[0])) {
                String[] strings = string.split(" ");
                switch (strings[0]) {
                    case "ALLOCATE":
                        id = controller.allocate(Allocation.fromString(string));
                        break;
                    case "SIP":
                        controller.registerSip(id, SIP.fromString(string));
                        break;
                    case "CHANGE":
                        controller.markChange(Month.valueOf(strings[4]), Change.fromString(string));
                        break;
                    case "BALANCE":
                        controller.printBalance(id, Month.valueOf(strings[1]));
                        break;
                    case "REBALANCE":
                        controller.printRebalancedAllocation(id);
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
