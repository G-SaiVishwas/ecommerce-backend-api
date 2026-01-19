package com.example.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartRequest {
    
    @NotBlank(message = "User ID required")
    private String userId;
    
    @NotBlank(message = "Product ID required")
    private String productId;
    
    @NotNull(message = "Quantity required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
