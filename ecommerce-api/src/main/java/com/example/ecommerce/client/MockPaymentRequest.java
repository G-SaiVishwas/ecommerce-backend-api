package com.example.ecommerce.client;

import lombok.Data;

@Data
public class MockPaymentRequest {
    private String orderId;
    private Double amount;
}
