package com.coffee.common.request;

import com.coffee.common.constant.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude
public class OrderStatusRequest {
    @NotNull
    private String orderId;

    @NotNull
    private Enum<OrderStatus> orderStatus;
}
