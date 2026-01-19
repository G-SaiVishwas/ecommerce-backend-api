package com.example.mockpayment.controller;

import com.example.mockpayment.dto.CreatePaymentRequest;
import com.example.mockpayment.model.Payment;
import com.example.mockpayment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(@RequestBody CreatePaymentRequest request) {
        Payment payment = paymentService.createPayment(request);
        return ResponseEntity.ok(payment);
    }
}
