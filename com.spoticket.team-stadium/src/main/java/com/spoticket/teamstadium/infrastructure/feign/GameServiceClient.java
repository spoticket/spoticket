package com.spoticket.teamstadium.infrastructure.feign;

import com.spoticket.teamstadium.application.dto.response.GameReadResponse;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "game")
public interface GameServiceClient {

  @GetMapping("/{teamId}")
  ApiResponse<List<GameReadResponse>> getGamesByTeamId(@PathVariable("teamId") UUID teamId);

}
