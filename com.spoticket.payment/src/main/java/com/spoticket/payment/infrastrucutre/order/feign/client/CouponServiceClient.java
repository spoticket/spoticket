package com.spoticket.payment.infrastrucutre.order.feign.client;

import com.spoticket.payment.infrastrucutre.order.feign.dto.CouponResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "coupon-service")
public interface CouponServiceClient {
    
    @GetMapping("/api/v1/coupons/{couponId}")
    CouponResponse getCoupon(@PathVariable UUID couponId);
    
}