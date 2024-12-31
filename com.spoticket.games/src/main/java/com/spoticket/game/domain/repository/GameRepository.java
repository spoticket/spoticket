package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {
}
