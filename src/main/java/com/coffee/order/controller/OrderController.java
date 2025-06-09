package com.coffee.order.controller;

import com.coffee.common.request.OrderRequest;
import com.coffee.common.request.OrderStatusRequest;
import com.coffee.common.response.CommonResponse;
import com.coffee.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public CommonResponse placeOrder(@Valid @RequestBody OrderRequest request){
        return orderService.placeOrder(request);
    }

    @PostMapping("/orders/status")
    public CommonResponse changeOrderStatusById(@Valid @RequestBody OrderStatusRequest request){
        return orderService.changeOrderStatusById(request);
    }

    //Check order status
    @GetMapping("/orders/{orderId}")
    public CommonResponse getOrderDetailsById(@PathVariable String orderId){
        return orderService.getOrderDetailsById(orderId);
    }
    //Queue position view
    @GetMapping("/orders/{orderId}/queue-position")
    public CommonResponse getCurrentQueuePosition(@PathVariable String orderId) {
        return orderService.getCurrentQueuePosition(orderId);
    }

    //Estimated time

    //Order Details



}
