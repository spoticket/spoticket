package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.TeamScore;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamScoreJpaRepository extends JpaRepository<TeamScore, UUID> {


  @Query("SELECT ts FROM p_team_scores ts "
      + "WHERE ts.teamId = :teamId "
      + "AND ts.isDeleted = false")
  Optional<TeamScore> findByTeamIdAndIsDeletedFalse(@Param("teamId") UUID teamId);
}
