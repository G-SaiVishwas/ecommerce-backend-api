package com.example.ecommerce.controller;

import com.example.ecommerce.dto.PaymentWebhookRequest;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class WebhookController {
    
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    
    @PostMapping("/payment")
    public ResponseEntity<Map<String, String>> handlePaymentWebhook(@RequestBody PaymentWebhookRequest request) {
        Payment payment = paymentRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + request.getOrderId()));
        
        payment.setStatus(request.getStatus());
        if (request.getPaymentId() != null) {
            payment.setPaymentId(request.getPaymentId());
        }
        paymentRepository.save(payment);
        
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + request.getOrderId()));
        
        if ("SUCCESS".equals(request.getStatus())) {
            order.setStatus("PAID");
        } else {
            order.setStatus("FAILED");
        }
        orderRepository.save(order);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Webhook processed successfully");
        response.put("orderId", request.getOrderId());
        response.put("status", request.getStatus());
        
        return ResponseEntity.ok(response);
    }
}
