package com.spoticket.user.presentation.controller;

import com.spoticket.user.application.service.UserCouponService;
import com.spoticket.user.dto.request.UserCouponRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    @PostMapping("/users/coupons")
    public ResponseEntity<?> issueCoupon(
            @RequestBody UserCouponRequestDto request,
            @RequestHeader("X-User-Id") UUID currentUserId,
            @RequestHeader("X-Role") String role
    ){
        return ResponseEntity.ok()
                .body(userCouponService.issueCoupon(currentUserId, role, request));
    }

    @PutMapping("/users/coupons/{userCouponId}")
    public ResponseEntity<?> useCoupon(
        @PathVariable UUID userCouponId,
        @RequestHeader("X-User-Id") UUID currentUserId
    ){
        return ResponseEntity.ok()
                .body(userCouponService.useCoupon(userCouponId, currentUserId));
    }

    @GetMapping("/users/coupons/{userCouponId}")
    public ResponseEntity<?> getUserCoupon(
            @PathVariable UUID userCouponId
    ){
        return ResponseEntity.ok()
                .body(userCouponService.getUserCoupon(userCouponId));
    }
}
