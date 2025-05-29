package com.coffee.common.constant;

public enum QueueStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    CLOSED("CLOSED");

    private final String status;

    public String valueOf() {
        return this.status;
    }

    private QueueStatus(String value) {
        this.status = value;
    }
}