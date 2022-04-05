package com.example.shippingbotserver.repository;

import com.example.shippingbotserver.entity.Lover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoverRepository extends JpaRepository<Lover, Long> {
}
