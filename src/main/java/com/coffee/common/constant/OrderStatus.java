package com.coffee.common.constant;

public enum OrderStatus {
    PLACED("PLACED"),
    CANCELLED("CANCELLED"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED");

    private final String status;

    public String valueOf() {

        return this.status;
    }

    private OrderStatus(String value) {

        this.status = value;
    }
}
