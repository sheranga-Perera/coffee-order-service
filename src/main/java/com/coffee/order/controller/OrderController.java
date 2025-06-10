package com.coffee.order.controller;

import com.coffee.common.request.OrderRequest;
import com.coffee.common.request.OrderStatusRequest;
import com.coffee.common.response.CommonResponse;
import com.coffee.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
@Tag(name = "Order Management", description = "APIs for managing coffee orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(
        summary = "Place a new order",
        description = "Creates a new coffee order and returns the order details with queue position",
        responses = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
        }
    )
    @PostMapping("/orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommonResponse> placeOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.placeOrder(request));
    }

    @Operation(
        summary = "Update order status",
        description = "Updates the status of an existing order",
        responses = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        }
    )
    @PostMapping("/orders/status")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<CommonResponse> updateOrderStatus(@Valid @RequestBody OrderStatusRequest request) {
        return ResponseEntity.ok(orderService.changeOrderStatusById(request));
    }

    @Operation(
        summary = "Get order details",
        description = "Retrieves the details of a specific order",
        responses = {
            @ApiResponse(responseCode = "200", description = "Order details retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        }
    )
    @GetMapping("/orders/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public ResponseEntity<CommonResponse> getOrderDetails(
            @Parameter(description = "ID of the order to retrieve")
            @PathVariable @NotBlank(message = "Order ID is required") String orderId) {
        return ResponseEntity.ok(orderService.getOrderDetailsById(orderId));
    }

    @Operation(
        summary = "Get queue position",
        description = "Retrieves the current queue position of an order",
        responses = {
            @ApiResponse(responseCode = "200", description = "Queue position retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order not found")
        }
    )
    @GetMapping("/orders/{orderId}/queue-position")
    @PreAuthorize("hasAnyRole('USER', 'STAFF')")
    public ResponseEntity<CommonResponse> getQueuePosition(
            @Parameter(description = "ID of the order to check queue position")
            @PathVariable @NotBlank(message = "Order ID is required") String orderId) {
        return ResponseEntity.ok(orderService.getCurrentQueuePosition(orderId));
    }
}
