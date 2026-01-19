package com.example.mockpayment.dto;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private String orderId;
    private Double amount;
}
