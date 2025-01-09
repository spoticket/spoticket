package com.spoticket.user.domain.repository;

import com.spoticket.user.domain.model.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserCouponRepository extends JpaRepository<UserCoupon, UUID> {
}
