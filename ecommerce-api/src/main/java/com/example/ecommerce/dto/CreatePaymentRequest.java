package com.example.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePaymentRequest {
    
    @NotBlank(message = "Order ID required")
    private String orderId;
    
    @NotNull(message = "Amount required")
    @Min(value = 0, message = "Amount must be positive")
    private Double amount;
}
