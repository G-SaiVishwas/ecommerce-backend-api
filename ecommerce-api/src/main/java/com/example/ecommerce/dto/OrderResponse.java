package com.example.ecommerce.dto;

import com.example.ecommerce.model.OrderItem;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private String userId;
    private Double totalAmount;
    private String status;
    private Instant createdAt;
    private List<OrderItem> items;
}
