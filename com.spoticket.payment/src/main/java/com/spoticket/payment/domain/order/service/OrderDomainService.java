package com.spoticket.payment.domain.order.service;

import com.spoticket.payment.application.order.dto.CreateOrderReq.OrderItemReq;
import com.spoticket.payment.domain.order.model.OrderItem;
import com.spoticket.payment.infrastrucutre.order.feign.client.CouponServiceClient;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDomainService {

    private CouponServiceClient couponServiceClient;

    /*

     */

    public long calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
            .mapToLong(OrderItem::getPrice)
            .sum();
    }
    public void validateHasCoupon(UUID couponId , UUID userId) {


        /*
            클라이언트에서 헤더로 넘어온 userId, 바디로 넘어온 couponId로
            해당 사용자가 쿠폰을 가지고 있는지 없는지 검증 후, 쿠폰 정보를 쿠폰 서비스에 요청해 응답받는다.
         */
        couponServiceClient.getCoupon(couponId);
    }
    public void getCouponInfo()
}
