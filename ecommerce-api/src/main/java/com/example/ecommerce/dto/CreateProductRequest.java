package com.example.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductRequest {
    
    @NotBlank(message = "Product name required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Price required")
    @Min(value = 0, message = "Price must be positive")
    private Double price;
    
    @NotNull(message = "Stock required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;
}
