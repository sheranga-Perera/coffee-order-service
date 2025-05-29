package com.coffee.common.request;

import lombok.Data;

@Data
public class OrderRequest {
    private String customerId;
    private String shopId;
    private String menuItem;
    private int quantity;
}
