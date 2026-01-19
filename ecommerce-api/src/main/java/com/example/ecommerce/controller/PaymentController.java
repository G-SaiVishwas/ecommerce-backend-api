package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CreatePaymentRequest;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        Payment payment = paymentService.createPayment(request);
        return ResponseEntity.ok(payment);
    }
}
