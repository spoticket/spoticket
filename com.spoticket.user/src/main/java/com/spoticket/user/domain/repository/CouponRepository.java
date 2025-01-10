package com.spoticket.user.domain.repository;

import com.spoticket.user.domain.model.entity.Coupon;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {

  Optional<Coupon> findByNameAndIsDeleted(String name, boolean b);
}
