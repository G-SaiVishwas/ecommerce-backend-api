package com.example.ecommerce.service;

import com.example.ecommerce.client.MockPaymentResponse;
import com.example.ecommerce.client.PaymentServiceClient;
import com.example.ecommerce.dto.CreatePaymentRequest;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentServiceClient paymentServiceClient;
    
    public Payment createPayment(CreatePaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Order status must be CREATED to process payment");
        }
        
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setStatus("PENDING");
        payment.setCreatedAt(Instant.now());
        payment = paymentRepository.save(payment);
        
        try {
            MockPaymentResponse mockResponse = paymentServiceClient.createPayment(
                    request.getOrderId(), 
                    request.getAmount()
            );
            
            payment.setPaymentId(mockResponse.getId());
            payment = paymentRepository.save(payment);
        } catch (Exception e) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            throw new RuntimeException("Payment service call failed: " + e.getMessage());
        }
        
        return payment;
    }
}
