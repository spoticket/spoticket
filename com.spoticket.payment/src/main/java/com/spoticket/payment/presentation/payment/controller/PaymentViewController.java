package com.spoticket.payment.presentation.payment.controller;

import com.spoticket.payment.application.payment.dto.PaymentEventRes;
import com.spoticket.payment.application.payment.service.PaymentService;
import com.spoticket.payment.domain.payment.model.PaymentConsumer;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class PaymentViewController {

    private final PaymentService paymentService;

    @GetMapping("/payment")
    public String paymentPage(@RequestParam UUID orderId, Model model) {
        PaymentEventRes eventRes = paymentService.getEvent(orderId);
        model.addAttribute("orderId",orderId );
        model.addAttribute("amount",eventRes.getAmount());
        model.addAttribute("itemName",eventRes.getItemName() );
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