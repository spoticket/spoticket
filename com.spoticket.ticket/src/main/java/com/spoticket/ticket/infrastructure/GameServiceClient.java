package com.spoticket.ticket.infrastructure;

import com.spoticket.ticket.application.dtos.response.GameResponse;
import com.spoticket.ticket.global.config.FeignConfig;
import com.spoticket.ticket.global.util.ApiResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "game", configuration = FeignConfig.class)
public interface GameServiceClient {

  @GetMapping("/api/v1/games/{gameId}")
  ApiResponse<GameResponse> getGame(@PathVariable UUID gameId);

}
