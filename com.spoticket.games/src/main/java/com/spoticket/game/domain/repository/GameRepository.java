package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.Game;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, UUID> {

}
