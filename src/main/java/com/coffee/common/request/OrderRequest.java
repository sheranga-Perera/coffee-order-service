package com.coffee.common.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {
    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotBlank(message = "Shop ID is required")
    private String shopId;

    @NotBlank(message = "Menu item is required")
    private String menuItem;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
}
