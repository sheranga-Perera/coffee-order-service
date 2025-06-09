package com.coffee.common.request;

import com.coffee.common.constant.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude
public class OrderStatusRequest {
    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;
}
