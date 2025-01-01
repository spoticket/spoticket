package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.dto.response.GameResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {

    Page<GameResponse> findAllByStadiumIdAndIsDeleteFalse(UUID stadiumId, Pageable pageable);

    Optional<Game> findByGameIdAndIsDeleteFalse(UUID gameId);

}
