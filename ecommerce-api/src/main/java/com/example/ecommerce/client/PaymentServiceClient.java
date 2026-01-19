package com.example.ecommerce.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PaymentServiceClient {
    
    private final RestTemplate restTemplate;
    
    @Value("${payment.service.url}")
    private String paymentServiceUrl;
    
    public MockPaymentResponse createPayment(String orderId, Double amount) {
        String url = paymentServiceUrl + "/payments/create";
        
        MockPaymentRequest request = new MockPaymentRequest();
        request.setOrderId(orderId);
        request.setAmount(amount);
        
        return restTemplate.postForObject(url, request, MockPaymentResponse.class);
    }
}
