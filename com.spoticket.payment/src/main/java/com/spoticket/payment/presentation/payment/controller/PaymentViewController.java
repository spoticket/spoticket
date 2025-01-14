package com.spoticket.payment.presentation.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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