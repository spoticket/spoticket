package com.spoticket.user.domain.repository;

import com.spoticket.user.domain.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    Optional<Coupon> findByNameAndIsDelete(String name, boolean b);
}
