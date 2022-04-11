package com.example.shippingbotserver.repository;

import com.example.shippingbotserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
