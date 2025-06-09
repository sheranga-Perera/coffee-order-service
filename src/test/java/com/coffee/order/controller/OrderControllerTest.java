package com.coffee.order.controller;

import com.coffee.common.config.TestSecurityConfig;
import com.coffee.common.constant.OrderStatus;
import com.coffee.common.exception.BusinessException;
import com.coffee.common.exception.ResourceNotFoundException;
import com.coffee.common.request.OrderRequest;
import com.coffee.common.request.OrderStatusRequest;
import com.coffee.common.response.CommonResponse;
import com.coffee.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequest validOrderRequest;
    private OrderStatusRequest validStatusRequest;
    private String validOrderId;
    private CommonResponse mockResponse;

    @BeforeEach
    void setUp() {
        validOrderId = UUID.randomUUID().toString();
        
        // Setup valid order request
        validOrderRequest = new OrderRequest();
        validOrderRequest.setCustomerId(UUID.randomUUID().toString());
        validOrderRequest.setShopId(UUID.randomUUID().toString());
        validOrderRequest.setMenuItem("Cappuccino");
        validOrderRequest.setQuantity(2);

        // Setup valid status request
        validStatusRequest = new OrderStatusRequest();
        validStatusRequest.setOrderId(validOrderId);
        validStatusRequest.setOrderStatus(OrderStatus.IN_PROGRESS);

        // Setup mock response
        mockResponse = new CommonResponse();
        mockResponse.setStatusCode("0");
        mockResponse.setStatusMessage("Success");
    }

    @Nested
    @DisplayName("Place Order Tests")
    class PlaceOrderTests {
        
        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Should create order when request is valid")
        void placeOrder_ValidRequest_ReturnsCreated() throws Exception {
            when(orderService.placeOrder(any(OrderRequest.class))).thenReturn(mockResponse);

            mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validOrderRequest)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value("0"));

            verify(orderService, times(1)).placeOrder(any(OrderRequest.class));
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("Should return bad request when request is invalid")
        void placeOrder_InvalidRequest_ReturnsBadRequest() throws Exception {
            OrderRequest invalidRequest = new OrderRequest();
            // Missing required fields

            mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(orderService, never()).placeOrder(any(OrderRequest.class));
        }

        @Test
        @DisplayName("Should return unauthorized when user is not authenticated")
        void placeOrder_UnauthenticatedUser_ReturnsUnauthorized() throws Exception {
            mockMvc.perform(post("/api/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validOrderRequest)))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());

            verify(orderService, never()).placeOrder(any(OrderRequest.class));
        }
    }

    @Nested
    @DisplayName("Change Order Status Tests")
    class ChangeOrderStatusTests {

        @Test
        @WithMockUser(roles = "STAFF")
        @DisplayName("Should update status when request is valid")
        void changeOrderStatus_ValidRequest_ReturnsOk() throws Exception {
            when(orderService.changeOrderStatusById(any(OrderStatusRequest.class))).thenReturn(mockResponse);

            mockMvc.perform(post("/api/orders/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validStatusRequest)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("0"));

            verify(orderService, times(1)).changeOrderStatusById(any(OrderStatusRequest.class));
        }

        @Test
        @WithMockUser(roles = "STAFF")
        @DisplayName("Should return not found when order doesn't exist")
        void changeOrderStatus_NonexistentOrder_ReturnsNotFound() throws Exception {
            when(orderService.changeOrderStatusById(any(OrderStatusRequest.class)))
                    .thenThrow(new ResourceNotFoundException("Order not found"));

            mockMvc.perform(post("/api/orders/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validStatusRequest)))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Get Order Details Tests")
    class GetOrderDetailsTests {

        @Test
        @WithMockUser(roles = {"USER", "STAFF"})
        @DisplayName("Should return order details when order exists")
        void getOrderDetails_ValidId_ReturnsOk() throws Exception {
            when(orderService.getOrderDetailsById(validOrderId)).thenReturn(mockResponse);

            mockMvc.perform(get("/api/orders/{orderId}", validOrderId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("0"));

            verify(orderService, times(1)).getOrderDetailsById(validOrderId);
        }

        @Test
        @WithMockUser(roles = {"USER", "STAFF"})
        @DisplayName("Should return not found when order doesn't exist")
        void getOrderDetails_InvalidId_ReturnsNotFound() throws Exception {
            when(orderService.getOrderDetailsById(any())).thenThrow(new ResourceNotFoundException("Order not found"));

            mockMvc.perform(get("/api/orders/{orderId}", "invalid-id"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Get Queue Position Tests")
    class GetQueuePositionTests {

        @Test
        @WithMockUser(roles = {"USER", "STAFF"})
        @DisplayName("Should return queue position when order exists")
        void getCurrentQueuePosition_ValidId_ReturnsOk() throws Exception {
            when(orderService.getCurrentQueuePosition(validOrderId)).thenReturn(mockResponse);

            mockMvc.perform(get("/api/orders/{orderId}/queue-position", validOrderId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("0"));

            verify(orderService, times(1)).getCurrentQueuePosition(validOrderId);
        }

        @Test
        @WithMockUser(roles = {"USER", "STAFF"})
        @DisplayName("Should return bad request when business rule is violated")
        void getCurrentQueuePosition_BusinessRuleViolation_ReturnsBadRequest() throws Exception {
            when(orderService.getCurrentQueuePosition(any()))
                    .thenThrow(new BusinessException("Order is not in queue"));

            mockMvc.perform(get("/api/orders/{orderId}/queue-position", validOrderId))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
} 