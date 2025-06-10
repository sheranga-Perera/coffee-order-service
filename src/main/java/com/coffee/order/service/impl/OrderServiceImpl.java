package com.coffee.order.service.impl;

import com.coffee.common.constant.CommonConstant;
import com.coffee.common.entity.Orders;
import com.coffee.common.entity.Queue;
import com.coffee.common.entity.Shop;
import com.coffee.common.mapper.CommonResponseMapper;
import com.coffee.common.repository.OrderRepository;
import com.coffee.common.repository.QueueRepository;
import com.coffee.common.repository.ShopRepository;
import com.coffee.common.request.OrderRequest;
import com.coffee.common.request.OrderStatusRequest;
import com.coffee.common.response.CommonResponse;
import com.coffee.common.response.OrderResponse;
import com.coffee.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final int AVERAGE_ORDER_PROCESSING_TIME = 5; // minutes

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Override
    @Transactional
    public CommonResponse placeOrder(OrderRequest request) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            // Check if shop exists and is open
            UUID shopUUID = UUID.fromString(request.getShopId());
            Shop shop = shopRepository.findById(shopUUID)
                    .orElseThrow(() -> new RuntimeException("Shop not found"));

            // Find available queue
            Queue queue = queueRepository.findFirstAvailableQueue(shopUUID)
                    .orElseThrow(() -> new RuntimeException("No available queue for the shop"));

            // Double-check queue capacity (in case of concurrent requests)
            synchronized (queue) {
                if (queue.getCurrentSize() >= queue.getMaxSize()) {
                    throw new RuntimeException("Queue is full. Please try again later.");
                }

                int position = queue.getCurrentSize() + 1;
                queue.setCurrentSize(position);
                queueRepository.save(queue);

                // Create and save order
                UUID customerId = UUID.fromString(request.getCustomerId());
                Orders order = createOrder(request, queue, position, customerId);
                Orders savedOrder = orderRepository.save(order);

                // Prepare response
                OrderResponse response = createOrderResponse(savedOrder, position);
                CommonResponseMapper.successResponseMapper(commonResponse, response);
            }
        } catch(Exception ex){
            LOGGER.error("Error while placing order exception ::{}", ex.getMessage());
            CommonResponseMapper.failureResponseMapper(commonResponse, ex);
        }
        return commonResponse;
    }

    private Orders createOrder(OrderRequest request, Queue queue, int position, UUID customerId) {
        Orders order = new Orders();
        order.setId(UUID.randomUUID());
        order.setCustomerId(customerId);
        order.setShopId(UUID.fromString(request.getShopId()));
        order.setMenuItem(request.getMenuItem());
        order.setQueue(queue);
        order.setQueuePosition(position);
        order.setStatus(CommonConstant.PLACED);
        order.setCreatedAt(Instant.now());
        return order;
    }

    private OrderResponse createOrderResponse(Orders order, int position) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId().toString());
        response.setStatus(order.getStatus());
        response.setQueuePosition(position);
        response.setEta(calculateETA(position));
        response.setMenuItem(order.getMenuItem());
        response.setShopId(order.getShopId().toString());
        response.setCustomerId(order.getCustomerId().toString());
        return response;
    }

    @Override
    @Transactional
    public CommonResponse changeOrderStatusById(OrderStatusRequest req) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            UUID orderId = UUID.fromString(req.getOrderId());
            Optional<Orders> ordersOptional = orderRepository.findById(orderId);

            if (ordersOptional.isPresent()) {
                Orders order = ordersOptional.get();
                String newStatus = req.getOrderStatus().toString();
                order.setStatus(newStatus);

                // Handle queue updates based on status change
                if (CommonConstant.CANCELLED.equals(newStatus)
                        || CommonConstant.COMPLETED.equals(newStatus)) {
                    Queue queue = order.getQueue();
                    if (queue != null && queue.getCurrentSize() > 0) {
                        queue.setCurrentSize(queue.getCurrentSize() - 1);
                        queueRepository.save(queue);

                        // Reorder queue positions for remaining orders
                        reorderQueuePositions(queue.getId(), order.getQueuePosition());
                    }
                }

                orderRepository.save(order);
                CommonResponseMapper.successResponseMapper(commonResponse, null);
            } else {
                throw new RuntimeException("Order not found");
            }

        } catch(Exception ex){
            CommonResponseMapper.failureResponseMapper(commonResponse, ex);
        }
        return commonResponse;
    }

    @Override
    public CommonResponse getOrderDetailsById(String id) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            UUID orderId = UUID.fromString(id);
            Optional<Orders> ordersOptional = orderRepository.findById(orderId);

            if (ordersOptional.isPresent()) {
                OrderResponse response = getOrderResponse(id, ordersOptional.get());

                CommonResponseMapper.successResponseMapper(commonResponse, response);
            } else {
                throw new RuntimeException("Order not found");
            }

        } catch(Exception ex){
            CommonResponseMapper.failureResponseMapper(commonResponse, ex);
        }
        return commonResponse;
    }

    private OrderResponse getOrderResponse(String id, Orders order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(id);
        response.setStatus(order.getStatus());
        response.setQueuePosition(order.getQueuePosition());
        response.setEta(calculateETA(order.getQueuePosition()));
        response.setMenuItem(order.getMenuItem());
        response.setShopId(order.getShopId().toString());
        response.setCustomerId(order.getCustomerId().toString());
        return response;
    }

    @Override
    public CommonResponse getCurrentQueuePosition(String orderId) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Optional<Orders> orderOptional = orderRepository.findById(UUID.fromString(orderId));

            if (orderOptional.isPresent()) {
                Orders order = orderOptional.get();
                OrderResponse response = new OrderResponse();
                response.setOrderId(orderId);
                response.setQueuePosition(order.getQueuePosition());
                response.setStatus(order.getStatus());
                response.setShopId(order.getShopId().toString());
                response.setEta(calculateETA(order.getQueuePosition()));

                CommonResponseMapper.successResponseMapper(commonResponse, response);
            } else {
                throw new RuntimeException("Order not found");
            }
        } catch (Exception ex) {
            CommonResponseMapper.failureResponseMapper(commonResponse, ex);
        }
        return commonResponse;
    }

    private String calculateETA(int queuePosition) {
        int estimatedMinutes = queuePosition * AVERAGE_ORDER_PROCESSING_TIME;
        return String.format("%d minutes", estimatedMinutes);
    }

    private void reorderQueuePositions(UUID queueId, int removedPosition) {
        // Update queue positions for all orders after the removed position
        orderRepository.findByQueueIdAndQueuePositionGreaterThan(queueId, removedPosition)
                .forEach(order -> {
                    order.setQueuePosition(order.getQueuePosition() - 1);
                    orderRepository.save(order);
                });
    }
}
