package com.spoticket.payment.presentation.order.controller;


import com.spoticket.payment.presentation.common.ApiSuccessResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @GetMapping("/{orderId}")
    public ApiSuccessResponse<Void> getOrder(@PathVariable Long orderId) {
        return ApiSuccessResponse.success(null);
    }
}
