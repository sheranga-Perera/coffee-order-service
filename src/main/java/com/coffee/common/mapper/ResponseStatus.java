package com.coffee.common.mapper;

/**
 * This class contains all the states related to response objects.
 *
 * @author Sheranga Perera
 * created 2025-05-25
 */
public enum ResponseStatus {
    SUCCESS("Success"),
    FAILURE( "Failure");

    private final String statusValue;

    public String valueOf() {

        return this.statusValue;
    }

    private ResponseStatus(String value) {
        this.statusValue = value;
    }
}
