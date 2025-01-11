package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.GameRank;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRankJpaRepository extends JpaRepository<GameRank, UUID> {

  List<GameRank> findAllByOrderByScoreDesc();

}
