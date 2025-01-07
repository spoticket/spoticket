package com.spoticket.user.test;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "game")
public interface GameClient {

  @GetMapping("/api/v1/games/test")
  String test();

}
