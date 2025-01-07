package com.spoticket.game;

import static com.spoticket.game.domain.model.Sport.BASKETBALL;
import static com.spoticket.game.domain.model.Sport.SOCCER;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.Sport;
import com.spoticket.game.domain.repository.GameJpaRepository;
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

  @Test
  void init() {
    UUID stadiumId = randomUUID();
    for (int i = 0; i < 100; i++) {
      String title = "Game " + i;
      LocalDateTime startTime = now().plusDays(i);
      Sport sport = (i % 2 == 0) ? SOCCER : BASKETBALL;
      String league = (i % 2 == 0) ? "Premier League" : "NBA";
      UUID homeTeamId = randomUUID();
      UUID awayTeamId = randomUUID();
      Game game = Game.of(title, startTime, sport, league, stadiumId, homeTeamId, awayTeamId);
      gameJpaRepository.save(game);
    }
  }

}
