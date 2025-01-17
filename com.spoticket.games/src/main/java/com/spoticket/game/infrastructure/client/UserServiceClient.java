package com.spoticket.game.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserServiceClient {

  @GetMapping("/api/v1/users/{userId}")
  ResponseEntity<?> selectUserById(@PathVariable UUID userId);

}
