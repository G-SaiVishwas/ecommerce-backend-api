package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CreateProductRequest;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(request);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String q) {
        List<Product> products = productService.searchProducts(q);
        return ResponseEntity.ok(products);
    }
}
