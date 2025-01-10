package com.spoticket.ticket.infrastructure;

import com.spoticket.ticket.application.dtos.response.UserResponseDto;
import com.spoticket.ticket.global.config.FeignConfig;
import com.spoticket.ticket.global.util.ApiResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", configuration = FeignConfig.class)
public interface UserServiceClient {

  @GetMapping("/api/v1/users/{userId}")
  ApiResponse<UserResponseDto> selectUserById(@PathVariable UUID userId);

}
