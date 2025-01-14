package com.spoticket.payment.presentation.order.controller;


import com.spoticket.payment.application.order.dto.CreateOrderReq;
import com.spoticket.payment.application.order.dto.OrderRes;
import com.spoticket.payment.application.order.service.OrderService;
import com.spoticket.payment.presentation.common.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiSuccessResponse<OrderRes> getOrders(CreateOrderReq req) {
       OrderRes orderRes =  orderService.createOrder(req);
        return ApiSuccessResponse.success(orderRes);
    }
    @GetMapping("/{orderId}")
    public ApiSuccessResponse<Void> getOrder(@PathVariable Long orderId) {
        return ApiSuccessResponse.success(null);
    }


}
