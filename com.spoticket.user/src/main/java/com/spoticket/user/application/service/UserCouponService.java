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
import com.spoticket.user.global.util.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.spoticket.user.global.exception.ErrorStatus.*;
import static com.spoticket.user.global.util.ResponseStatus.COUPON_ISSUE;
import static com.spoticket.user.global.util.ResponseStatus.COUPON_USED;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final StringRedisTemplate redisTemplate; // Redis를 위한 Template

    private static final String COUPON_STOCK_KEY = "coupon:stock:";

    @DistributedLock(key = "#couponLock")
    public SuccessResponse<?> issueCoupon(UUID currentUserId, String role, UserCouponRequestDto request) {

        // 1. Redis에서 쿠폰 재고 확인 및 감소
        String redisKey = COUPON_STOCK_KEY + request.couponId();
        Long remainingStock = redisTemplate.opsForValue().decrement(redisKey);

        if (remainingStock == null || remainingStock < 0) {
            // Redis 재고 부족 또는 키 없음
            redisTemplate.opsForValue().increment(redisKey); // 재고 복구
            throw new CustomException(COUPON_ENABLE);
        }

        // 2. 쿠폰 정보와 사용자 정보 조회
        Coupon coupon = couponRepository.findById(request.couponId()).orElseThrow(
                () -> new CustomException(COUPON_NOT_FOUND)
        );
        User user = userRepository.findById(currentUserId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        // 3. 쿠폰 발급
        UserCoupon userCoupon = UserCoupon.of(user, coupon);
        userCoupon.getCoupon().issue();

        // 4. 데이터베이스 저장
        userCouponRepository.save(userCoupon);

        log.info("🎫🎫 [UserCouponService] 쿠폰 발급 완료 사용자 : {}, 남은 쿠폰 : {} 🎫🎫", user.getName(), coupon.getStock());

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
                userCoupon.getStatus(),
                userCoupon.getCoupon().getName(),
                userCoupon.getCoupon().getExpirationAt(),
                userCoupon.getCoupon().getType(),
                userCoupon.getCoupon().getDiscountRate(),
                userCoupon.getCoupon().getIsActive()
        );

        return SuccessResponse.ok(response);
    }
}
