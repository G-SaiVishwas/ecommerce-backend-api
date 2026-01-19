package com.example.mockpayment.service;

import com.example.mockpayment.dto.CreatePaymentRequest;
import com.example.mockpayment.dto.WebhookCallbackRequest;
import com.example.mockpayment.model.Payment;
import com.example.mockpayment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;
    
    @Value("${ecommerce.webhook.url}")
    private String webhookUrl;
    
    public Payment createPayment(CreatePaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setStatus("PENDING");
        payment.setCreatedAt(Instant.now());
        payment = paymentRepository.save(payment);
        
        processPaymentAsync(payment.getId(), request.getOrderId());
        
        return payment;
    }
    
    @Async
    public void processPaymentAsync(String paymentId, String orderId) {
        try {
            Thread.sleep(3000);
            
            WebhookCallbackRequest webhook = new WebhookCallbackRequest();
            webhook.setOrderId(orderId);
            webhook.setStatus("SUCCESS");
            webhook.setPaymentId(paymentId);
            
            restTemplate.postForObject(webhookUrl, webhook, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
