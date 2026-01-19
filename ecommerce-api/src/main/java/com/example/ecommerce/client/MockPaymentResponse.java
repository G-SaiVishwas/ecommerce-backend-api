package com.example.ecommerce.client;

import lombok.Data;

@Data
public class MockPaymentResponse {
    private String id;
    private String orderId;
    private Double amount;
    private String status;
}
