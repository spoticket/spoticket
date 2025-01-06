package com.spoticket.payment.presentation.payment.controller;

import com.spoticket.payment.presentation.common.ApiSuccessResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @GetMapping("/{paymentId}")
    public ApiSuccessResponse<Void> getPayment(@PathVariable Long paymentId) {
        return ApiSuccessResponse.success(null);
    }
}
