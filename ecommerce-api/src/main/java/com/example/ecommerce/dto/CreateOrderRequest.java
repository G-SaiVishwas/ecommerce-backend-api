package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequest {
    
    @NotBlank(message = "User ID required")
    private String userId;
}
