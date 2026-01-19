package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CreateOrderRequest;
import com.example.ecommerce.dto.OrderDetailResponse;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetails(@PathVariable String orderId) {
        OrderDetailResponse order = orderService.getOrderDetails(orderId);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable String userId) {
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable String orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }
}
