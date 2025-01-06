package com.spoticket.user.test;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "team-stadium")
public interface TeamStadiumClient {

  @GetMapping("/api/v1/teams/test")
  String test();

}
