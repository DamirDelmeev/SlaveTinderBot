package com.example.shippingbotserver.repository;

import com.example.shippingbotserver.model.LoverModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoverModelRepository extends JpaRepository<LoverModel, Long> {
}
