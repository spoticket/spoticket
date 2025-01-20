package com.spoticket.payment.infrastrucutre.order.feign.client;


import com.spoticket.payment.infrastrucutre.order.feign.dto.UserCouponResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface CouponServiceClient {
    
    @GetMapping("/api/v1/users/coupons/{userCouponId}")
    UserCouponResponseDto getCoupon(@PathVariable UUID userCouponId);
    
}