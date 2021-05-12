package com.geektrust.mymoney.model.portfolio;

import com.geektrust.mymoney.controller.Controller;
import com.geektrust.mymoney.controller.ControllerFactory;
import com.geektrust.mymoney.model.calender.Month;
import com.geektrust.mymoney.model.portfolio.allocation.Allocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

public class TestController {

    private Controller controller;

    @Before
    public void setup() {
        controller = ControllerFactory.instance().controller();
        controller.allocate(Allocation.fromString("ALLOCATE 8240 4300 1520"));
    }

    @Test
    public void throwsIllegalArgumentExceptionOnNullId() {
        Assert.assertThrows(IllegalArgumentException.class, () -> controller.printBalance(null, Month.JANUARY));
    }

    @Test
    public void throwsNoSuchElementExceptionOnInvalidId() {
        Assert.assertThrows(NoSuchElementException.class, () -> controller.printBalance("invalid_id", Month.JANUARY));
    }
}
