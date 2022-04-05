package com.example.shippingbotserver.repository;

import com.example.shippingbotserver.entity.Lover;
import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoverRepository extends JpaRepository<Lover, Long> {
}
