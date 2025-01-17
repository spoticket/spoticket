package com.spoticket.game;

import static com.spoticket.game.domain.model.Sport.BASKETBALL;
import static com.spoticket.game.domain.model.Sport.SOCCER;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.Sport;
import com.spoticket.game.infrastructure.repository.GameJpaRepository;
import com.spoticket.game.infrastructure.repository.LeagueJpaRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Commit
class GameDataInitializationTest {

  @Autowired
  private GameJpaRepository gameJpaRepository;

  @Autowired
  private LeagueJpaRepository leagueJpaRepository;

  @Test
  void init() {
    UUID stadiumId = randomUUID();

    League soccerLeague = League.builder()
        .name("Premier League")
        .sport(Sport.SOCCER)
        .season(2025)
        .build();

    League basketballLeague = League.builder()
        .name("NBA")
        .sport(Sport.BASKETBALL)
        .season(2025)
        .build();

    leagueJpaRepository.save(soccerLeague);
    leagueJpaRepository.save(basketballLeague);

    for (int i = 0; i < 100; i++) {
      String title = "Game " + i;
      LocalDateTime startTime = now().plusDays(i);
      Sport sport = (i % 2 == 0) ? SOCCER : BASKETBALL;
      League league = (i % 2 == 0) ? soccerLeague : basketballLeague;
      UUID homeTeamId = randomUUID();
      UUID awayTeamId = randomUUID();
      Game game = Game.of(title, startTime, sport, league, stadiumId, homeTeamId, awayTeamId);
      gameJpaRepository.save(game);
    }
  }

}
