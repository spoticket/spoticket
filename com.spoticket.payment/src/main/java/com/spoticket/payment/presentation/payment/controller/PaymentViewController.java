package com.spoticket.payment.presentation.payment.controller;

import com.spoticket.payment.application.order.service.OrderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PaymentViewController {


    @GetMapping("/payment")
    public String paymentPage() {
        return "payment";
    }

    @GetMapping("/payment/success")
    public String successPage() {
        return "success";
    }

    @GetMapping("/payment/fail")
    public String failPage() {
        return "fail";
    }
}