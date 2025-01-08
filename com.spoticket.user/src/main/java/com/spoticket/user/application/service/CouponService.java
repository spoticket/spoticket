package com.spoticket.user.application.service;

import com.spoticket.user.domain.model.entity.Coupon;
import com.spoticket.user.domain.repository.CouponRepository;
import com.spoticket.user.dto.request.CouponCreateRequestDto;
import com.spoticket.user.dto.request.CouponUpdateRequestDto;
import com.spoticket.user.dto.response.CouponResponseDto;
import com.spoticket.user.global.exception.CustomException;
import com.spoticket.user.global.util.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.spoticket.user.global.exception.ErrorStatus.COUPON_DUPLICATE;
import static com.spoticket.user.global.exception.ErrorStatus.COUPON_NOT_FOUND;
import static com.spoticket.user.global.util.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public SuccessResponse<?> create(CouponCreateRequestDto request) {

        if (couponRepository.findByNameAndIsDelete(request.name(), false).isPresent()) {
            throw new CustomException(COUPON_DUPLICATE);
        }

        Coupon coupon = Coupon.of(
                request.name(),
                request.expirationAt(),
                request.isAble(),
                request.type(),
                request.discountRate(),
                request.stock(),
                request.isActive()
        );

        couponRepository.save(coupon);

        return SuccessResponse.of(COUPON_CREATE);
    }

    @Transactional
    public SuccessResponse<?> update(UUID couponId, CouponUpdateRequestDto request) {

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new CustomException(COUPON_NOT_FOUND)
        );

        coupon.update(
                request.name(),
                request.expirationAt(),
                request.isAble(),
                request.type(),
                request.discountRate(),
                request.stock(),
                request.isActive()
        );

        return SuccessResponse.of(COUPON_UPDATE);
    }


    @Transactional
    public SuccessResponse<?> delete(UUID couponId) {

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new CustomException(COUPON_NOT_FOUND)
        );

        // 임시
        coupon.delete(UUID.fromString("SYSTEM"));

        return SuccessResponse.of(COUPON_DELETE);
    }

    @Transactional(readOnly = true)
    public SuccessResponse<?> select(UUID couponId) {

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new CustomException(COUPON_NOT_FOUND)
        );

        CouponResponseDto response = new CouponResponseDto(
                coupon.getCouponId(),
                coupon.getName(),
                coupon.getExpirationAt(),
                coupon.getIsAble(),
                coupon.getType(),
                coupon.getDiscountRate(),
                coupon.getStock(),
                coupon.getIsActive()
        );

        return SuccessResponse.ok(response);
    }
}
