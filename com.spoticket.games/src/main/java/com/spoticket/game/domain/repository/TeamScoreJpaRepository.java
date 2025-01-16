package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.TeamScore;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamScoreJpaRepository extends JpaRepository<TeamScore, UUID> {


  Optional<TeamScore> findByTeamIdAndIsDeletedFalse(UUID teamId);
}
