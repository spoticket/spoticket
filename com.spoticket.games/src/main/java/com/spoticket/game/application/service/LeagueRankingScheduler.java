package com.spoticket.game.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeagueRankingScheduler {


  private final LeagueService leagueService;

  @Scheduled(cron = "0 0 0 * * ?")
  public void updateLeagueRankings() {
    leagueService.updateRankings();
  }

}
