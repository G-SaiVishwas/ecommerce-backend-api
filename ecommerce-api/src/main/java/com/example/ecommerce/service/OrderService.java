package com.example.ecommerce.service;

import com.example.ecommerce.dto.CreateOrderRequest;
import com.example.ecommerce.dto.OrderDetailResponse;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(request.getUserId());
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        double totalAmount = 0.0;
        
        for (CartItem item : cartItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
            
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            totalAmount += product.getPrice() * item.getQuantity();
        }
        
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setTotalAmount(totalAmount);
        order.setStatus("CREATED");
        order.setCreatedAt(Instant.now());
        order = orderRepository.save(order);
        
        for (CartItem item : cartItems) {
            Product product = productRepository.findById(item.getProductId()).get();
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItemRepository.save(orderItem);
            
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
        
        cartItemRepository.deleteByUserId(request.getUserId());
        
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(orderItems);
        
        return response;
    }
    
    public OrderDetailResponse getOrderDetails(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        Payment payment = paymentRepository.findByOrderId(orderId).orElse(null);
        
        OrderDetailResponse response = new OrderDetailResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setPayment(payment);
        response.setItems(items);
        
        return response;
    }
    
    public List<OrderResponse> getUserOrders(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        
        return orders.stream().map(order -> {
            List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
            
            OrderResponse response = new OrderResponse();
            response.setId(order.getId());
            response.setUserId(order.getUserId());
            response.setTotalAmount(order.getTotalAmount());
            response.setStatus(order.getStatus());
            response.setCreatedAt(order.getCreatedAt());
            response.setItems(items);
            
            return response;
        }).toList();
    }
    
    @Transactional
    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }
        
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
            
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
        
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
}
