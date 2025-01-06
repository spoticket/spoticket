package com.spoticket.ticket.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/api/v1/tickets/test")
  public String test() {
    return "TestController.test from ticket service";
  }

}
