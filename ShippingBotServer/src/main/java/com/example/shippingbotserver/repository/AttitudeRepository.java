package com.example.shippingbotserver.repository;

import com.example.shippingbotserver.entity.Attitude;
import com.example.shippingbotserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttitudeRepository extends JpaRepository<Attitude, Long> {
    List<Attitude> findAllByInitIdAndNameOfAction(User initId, String nameOfAction);

    List<Attitude> findAllByTargetIdAndNameOfAction(User targetId, String name);

    List<Attitude> findAllByInitId(User user);

    List<Attitude> findAllByTargetId(User user);

    Attitude findByInitIdAndTargetIdAndNameOfAction(User init, User target, String att);
}
