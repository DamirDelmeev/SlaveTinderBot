package com.example.shippingbotserver.repository;

import com.example.shippingbotserver.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, Long> {
}
