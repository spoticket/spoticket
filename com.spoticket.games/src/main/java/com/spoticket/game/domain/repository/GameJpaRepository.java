package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.Game;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameJpaRepository extends JpaRepository<Game, UUID> {

  Optional<Game> findByGameIdAndIsDeletedFalse(UUID gameId);

}
