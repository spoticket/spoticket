package com.spoticket.payment.domain.order.service;

import com.spoticket.payment.domain.order.model.OrderItem;
import com.spoticket.payment.infrastrucutre.order.feign.client.CouponServiceClient;
import com.spoticket.payment.infrastrucutre.order.feign.dto.UserCouponResponseDto;
import com.spoticket.payment.presentation.common.ApiSuccessResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDomainService {

    private static final Logger log = LoggerFactory.getLogger(OrderDomainService.class);
    private final CouponServiceClient couponServiceClient;


    public long calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
            .mapToLong(OrderItem::getPrice)
            .sum();
    }

    public ApiSuccessResponse<UserCouponResponseDto> validateUserCoupon(UUID couponId, UUID userId) {
        ApiSuccessResponse<UserCouponResponseDto> res = couponServiceClient.getCoupon(couponId);
        UserCouponResponseDto responseDto = res.getData();
        log.info("쿠폰 정보  - Coupon Id: {}, User Id: {},  DiscountRate: {} ", responseDto.couponId(), responseDto.userId(), responseDto.discountRate());
        /*
            클라언트에서 받은 쿠폰 id를 통해서 현재 사용자가 가지고있는 쿠폰인지 검증한다.
         */
        if (!userId.equals(responseDto.userId())) {
            throw new IllegalStateException("사용자의 쿠폰이 아닙니다.");
        }
        if (!responseDto.isActive()) {
            throw new IllegalStateException("만료된 쿠폰입니다.");
        }
        return res;
    }

    public long calculatePriceWithCouponDiscount(long amount, Double discountRate) {
        return amount * (100 - discountRate.longValue()) / 100;
    }
}
