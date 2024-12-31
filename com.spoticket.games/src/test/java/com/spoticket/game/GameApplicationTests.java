package com.spoticket.game;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.Sport;
import com.spoticket.game.domain.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
@Transactional
@Commit
class GameApplicationTests {

    @Autowired
    private GameRepository gameRepository;

    @Test
    void contextLoads() {
        Game game = Game.of("title", LocalDateTime.now(), Sport.BASEBALL, "league", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        gameRepository.save(game);
    }

}
