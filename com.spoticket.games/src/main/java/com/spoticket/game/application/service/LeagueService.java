package com.spoticket.game.application.service;

import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.repository.LeagueJpaRepository;
import com.spoticket.game.global.exception.CustomException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeagueService {

  private final LeagueJpaRepository leagueJpaRepository;

  public League findById(UUID leagueId) {
    return leagueJpaRepository.findByLeagueIdAndIsDeletedFalse(leagueId)
        .orElseThrow(() -> new CustomException(400, "해당하는 리그가 없습니다"));
  }
}
