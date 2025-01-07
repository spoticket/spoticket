package com.spoticket.game.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/api/v1/games/test")
  public String test() {
    return "TestController.test from game service";
  }

}
