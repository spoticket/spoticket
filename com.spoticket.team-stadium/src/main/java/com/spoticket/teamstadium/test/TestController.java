package com.spoticket.teamstadium.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/api/v1/teams/test")
  public String test() {
    return "TestController.test from team-stadium service";
  }

}
