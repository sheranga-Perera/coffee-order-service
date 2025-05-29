package com.coffee.common.response;

import lombok.Data;

@Data
public class OrderResponse {
    private String orderId;
    private String eta;
    private String shopId;
    private String status;
    private String menuItem;
    private String customerId;
    private int queuePosition;
}
