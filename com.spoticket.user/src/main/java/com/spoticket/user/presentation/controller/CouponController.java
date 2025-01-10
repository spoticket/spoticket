package com.spoticket.user.presentation.controller;

import com.spoticket.user.application.service.CouponService;
import com.spoticket.user.dto.request.CouponCreateRequestDto;
import com.spoticket.user.dto.request.CouponUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    ResponseEntity<?> create(
            @RequestBody CouponCreateRequestDto request
    ) {
        return ResponseEntity.ok()
                .body(couponService.create(request));
    }

    @PutMapping("/{couponId}")
    ResponseEntity<?> update(
            @RequestBody CouponUpdateRequestDto request,
            @PathVariable UUID couponId
    ) {
        return ResponseEntity.ok()
                .body(couponService.update(couponId, request));
    }

    @DeleteMapping("/{couponId}")
    ResponseEntity<?> delete(
            @PathVariable UUID couponId
    ) {
        return ResponseEntity.ok()
                .body(couponService.delete(couponId));
    }

    @GetMapping("/{couponId}")
    ResponseEntity<?> select(
            @PathVariable UUID couponId
    ){
        return ResponseEntity.ok()
                .body(couponService.select(couponId));
    }
}
