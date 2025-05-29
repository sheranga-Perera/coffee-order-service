package com.coffee.order.service;

import com.coffee.common.request.OrderRequest;
import com.coffee.common.request.OrderStatusRequest;
import com.coffee.common.response.CommonResponse;

public interface OrderService {
    CommonResponse placeOrder(OrderRequest request);

    CommonResponse changeOrderStatusById(OrderStatusRequest request);

    CommonResponse getOrderDetailsById(String orderId);


    CommonResponse getCurrentQueuePosition(String orderId);
}
