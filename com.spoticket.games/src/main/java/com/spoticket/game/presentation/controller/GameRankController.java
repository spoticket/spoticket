package com.spoticket.game.presentation.controller;

import static com.spoticket.game.global.util.ResponseUtils.ok;

import com.spoticket.game.application.service.GameRankService;
import com.spoticket.game.dto.response.GameResponse;
import com.spoticket.game.global.util.ResponseUtils.DataResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameRankController {

  private final GameRankService gameRankService;

  @GetMapping("/ranking")
  public DataResponse<List<GameResponse>> getGameRanking() {
    return ok(gameRankService.getGameRanks());
  }

}
