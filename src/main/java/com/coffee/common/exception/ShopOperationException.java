package com.coffee.common.exception;

public class ShopOperationException extends RuntimeException {
    public ShopOperationException(String message) {
        super(message);
    }

    public ShopOperationException(String message, Throwable cause) {
        super(message, cause);
    }
} 