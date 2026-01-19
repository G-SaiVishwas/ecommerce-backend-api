package com.example.ecommerce.controller;

import com.example.ecommerce.dto.AddToCartRequest;
import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    
    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@Valid @RequestBody AddToCartRequest request) {
        CartItem cartItem = cartService.addToCart(request);
        return ResponseEntity.ok(cartItem);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCart(@PathVariable String userId) {
        List<CartItemResponse> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(items);
    }
    
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Map<String, String>> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");
        return ResponseEntity.ok(response);
    }
}
