package com.spoticket.payment.presentation.order.controller;


import com.spoticket.payment.application.order.dto.OrderCreateReq;
import com.spoticket.payment.application.order.dto.OrderRes;
import com.spoticket.payment.application.order.service.OrderService;
import com.spoticket.payment.application.payment.dto.PaymentCreateReq;
import com.spoticket.payment.presentation.common.ApiSuccessResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiSuccessResponse<OrderRes> createOrder(@RequestBody OrderCreateReq orderCreateReq) {
        log.info("Order creation request - userId: {}", orderCreateReq.userId());
        OrderRes orderRes = orderService.createOrder(orderCreateReq);
        return ApiSuccessResponse.success(orderRes);
    }

    @GetMapping("/{orderId}")
    public ApiSuccessResponse<OrderRes> getOrder(@PathVariable UUID orderId) {
        OrderRes orderRes = orderService.getOrder(orderId);
        return ApiSuccessResponse.success(orderRes);
    }
//
//    @PostMapping("/{orderId}/payment")
//    public ApiSuccessResponse<PaymentCreateReq> requestPayment(@PathVariable UUID orderId) {
//        return orderService.requestPayment(orderId);
//    }
}
