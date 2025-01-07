package com.spoticket.user.test;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ticket")
public interface TicketClient {

  @GetMapping("/api/v1/tickets/test")
  String test();

}
