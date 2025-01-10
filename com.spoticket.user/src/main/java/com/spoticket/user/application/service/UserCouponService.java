package com.spoticket.user.application.service;

import com.spoticket.user.domain.model.entity.Coupon;
import com.spoticket.user.domain.model.entity.User;
import com.spoticket.user.domain.model.entity.UserCoupon;
import com.spoticket.user.domain.repository.CouponRepository;
import com.spoticket.user.domain.repository.UserCouponRepository;
import com.spoticket.user.domain.repository.UserRepository;
import com.spoticket.user.dto.request.UserCouponRequestDto;
import com.spoticket.user.dto.response.UserCouponResponseDto;
import com.spoticket.user.global.exception.CustomException;
import com.spoticket.user.global.util.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.spoticket.user.global.exception.ErrorStatus.*;
import static com.spoticket.user.global.util.ResponseStatus.COUPON_ISSUE;
import static com.spoticket.user.global.util.ResponseStatus.COUPON_USED;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public SuccessResponse<?> issueCoupon(UUID currentUserId, String role, UserCouponRequestDto request) {

        Coupon coupon = couponRepository.findById(request.couponId()).orElseThrow(
                () -> new CustomException(COUPON_NOT_FOUND)
        );

        if (!coupon.getIsAble() || coupon.getStock() > 0) {
            throw new CustomException(COUPON_ENABLE);
        }

        User user = userRepository.findById(currentUserId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        UserCoupon userCoupon = UserCoupon.of(user, coupon);

        userCouponRepository.save(userCoupon);

        return SuccessResponse.of(COUPON_ISSUE);
    }


    @Transactional
    public SuccessResponse<?> useCoupon(UUID userCouponId, UUID currentUserId) {

        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow(
                () -> new CustomException(COUPON_NOT_FOUND)
        );

        if (!userCoupon.getUser().getUserId().equals(currentUserId)){
            throw new CustomException(COUPON_MATCH);
        }

        if (!userCoupon.getCoupon().getIsActive()){
            throw new CustomException(COUPON_DEACTIVATE);
        }

        userCoupon.use();

        return SuccessResponse.of(COUPON_USED);
    }

    @Transactional(readOnly = true)
    public SuccessResponse<?> getUserCoupon(UUID userCouponId) {

        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow(
                () -> new CustomException(COUPON_NOT_FOUND)
        );

        UserCouponResponseDto  response = new UserCouponResponseDto(
                userCoupon.getUserCouponId(),
                userCoupon.getUser().getUserId(),
                userCoupon.getCoupon().getCouponId(),
                userCoupon.getStatus()
        );

        return SuccessResponse.ok(response);
    }
}
