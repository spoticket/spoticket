package com.spoticket.user.test;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "payment")
public interface PaymentClient {

  @GetMapping("/api/v1/payments/test")
  String test();

}
