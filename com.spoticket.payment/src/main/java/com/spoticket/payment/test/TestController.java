package com.spoticket.payment.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/api/v1/payments/test")
  public String test() {
    return "TestController.test from payment service";
  }

}
