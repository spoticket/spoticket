package com.spoticket.teamstadium.infrastructure.feign;

import com.spoticket.teamstadium.application.dto.response.PagedGameResponse;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "game")
public interface GameServiceClient {

  @GetMapping("/api/v1/games")
  ApiResponse<PagedGameResponse> getGamesByTeamId(
      @RequestParam("teamId") UUID teamId,
      @RequestParam("page") int page,
      @RequestParam("size") int size);

  @GetMapping("/api/v1/games")
  ApiResponse<PagedGameResponse> getGamesByStadiumId(
      @RequestParam("stadiumId") UUID stadiumId,
      @RequestParam("page") int page,
      @RequestParam("size") int size);
}

