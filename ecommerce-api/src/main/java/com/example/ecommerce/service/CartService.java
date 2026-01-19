package com.example.ecommerce.service;

import com.example.ecommerce.dto.AddToCartRequest;
import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    
    public CartItem addToCart(AddToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }
        
        Optional<CartItem> existing = cartItemRepository
                .findByUserIdAndProductId(request.getUserId(), request.getProductId());
        
        if (existing.isPresent()) {
            CartItem item = existing.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            
            if (product.getStock() < newQuantity) {
                throw new RuntimeException("Insufficient stock for total quantity");
            }
            
            item.setQuantity(newQuantity);
            return cartItemRepository.save(item);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(request.getUserId());
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            return cartItemRepository.save(cartItem);
        }
    }
    
    public List<CartItemResponse> getCartItems(String userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        
        return cartItems.stream().map(item -> {
            CartItemResponse response = new CartItemResponse();
            response.setId(item.getId());
            response.setProductId(item.getProductId());
            response.setQuantity(item.getQuantity());
            
            Product product = productRepository.findById(item.getProductId())
                    .orElse(null);
            response.setProduct(product);
            
            return response;
        }).collect(Collectors.toList());
    }
    
    @Transactional
    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
