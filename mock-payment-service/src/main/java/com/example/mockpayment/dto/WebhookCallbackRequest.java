package com.example.mockpayment.dto;

import lombok.Data;

@Data
public class WebhookCallbackRequest {
    private String orderId;
    private String status;
    private String paymentId;
}
