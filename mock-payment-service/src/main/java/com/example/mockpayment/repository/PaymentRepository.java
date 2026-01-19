package com.example.mockpayment.repository;

import com.example.mockpayment.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
}
