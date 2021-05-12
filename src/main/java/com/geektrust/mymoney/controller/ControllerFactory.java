package com.geektrust.mymoney.controller;

import java.util.Objects;

public class ControllerFactory {

    private static ControllerFactory instance;

    private ControllerFactory() {
    }

    public static ControllerFactory instance() {
        if (Objects.isNull(instance)) {
            instance = new ControllerFactory();
        }
        return instance;
    }

    public Controller controller() {
        return new ControllerImpl();
    }
}
