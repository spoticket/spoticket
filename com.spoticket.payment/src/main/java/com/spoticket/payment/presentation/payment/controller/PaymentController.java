package com.spoticket.payment.presentation.payment.controller;

import com.spoticket.payment.application.payment.dto.PaymentCreateReq;
import com.spoticket.payment.application.payment.dto.PaymentRes;
import com.spoticket.payment.application.payment.service.PaymentService;
import com.spoticket.payment.infrastrucutre.toss.dto.TossCancelPaymentReq;
import com.spoticket.payment.infrastrucutre.toss.dto.TossPaymentReq;
import com.spoticket.payment.infrastrucutre.toss.dto.TossPaymentRes;
import com.spoticket.payment.presentation.common.ApiSuccessResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping
    public ApiSuccessResponse<Void> createPayment(@RequestBody PaymentCreateReq req) {
        paymentService.createOrReusePayment(req.getOrderId(), req.getAmount());
        return ApiSuccessResponse.success(null);
    }

    @PostMapping("/cancel")
    public ApiSuccessResponse<TossPaymentRes> cancelPayment(@RequestBody TossCancelPaymentReq req) {
        TossPaymentRes res = paymentService.cancelPayment(req.paymentKey(), req.cancelReason());
        return ApiSuccessResponse.success(res);
    }

    @PostMapping("/confirm")
    public ApiSuccessResponse<TossPaymentRes> confirmPayment(@RequestBody TossPaymentReq req) {
        TossPaymentRes res = paymentService.confirmPayment(req);
        return ApiSuccessResponse.success(res);
    }

    // 아직 미구현
//    @PostMapping("/fail")
//    public ApiSuccessResponse<TossPaymentRes> failPayment(@RequestBody TossPaymentReq req) {
//        return ApiSuccessResponse.success(null);
//    }

    @GetMapping("/{paymentId}")
    public ApiSuccessResponse<PaymentRes> getPayment(@PathVariable UUID paymentId) {
        PaymentRes res = paymentService.getPayment(paymentId);
        return ApiSuccessResponse.success(res);
    }

}