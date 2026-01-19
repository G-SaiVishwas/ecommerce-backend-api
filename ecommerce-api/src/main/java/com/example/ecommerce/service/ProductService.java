package com.example.ecommerce.service;

import com.example.ecommerce.dto.CreateProductRequest;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public Product createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        
        return productRepository.save(product);
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    public List<Product> searchProducts(String query) {
        return productRepository.searchByName(query);
    }
}
