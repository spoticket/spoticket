package com.spoticket.payment.domain.order.service;

import com.spoticket.payment.domain.order.model.OrderItem;
import com.spoticket.payment.infrastrucutre.order.feign.client.CouponServiceClient;
import com.spoticket.payment.infrastrucutre.order.feign.dto.UserCouponResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDomainService {

    private final CouponServiceClient couponServiceClient;


    public long calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
            .mapToLong(OrderItem::getPrice)
            .sum();
    }

    public UserCouponResponseDto validateUserCoupon(UUID couponId, UUID userId) {
        UserCouponResponseDto res = couponServiceClient.getCoupon(couponId);
        /*
            클라언트에서 받은 쿠폰 id를 통해서 현재 사용자가 가지고있는 쿠폰인지 검증한다.
         */
        if (!userId.equals(res.userId())) {
            throw new IllegalStateException("사용자의 쿠폰이 아닙니다.");
        }
        if (!res.isActive()) {
            throw new IllegalStateException("만료된 쿠폰입니다.");
        }
        return res;
    }

    public long calculatePriceWithCouponDiscount(long amount, Double discountRate) {
        return amount * (100 - discountRate.longValue()) / 100;
    }
}
