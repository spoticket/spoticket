package com.spoticket.game.infrastructure.repository;

import com.spoticket.game.domain.model.TeamScore;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Query("""
          SELECT ts 
          FROM p_team_scores ts 
          WHERE ts.teamId IN (
              SELECT lt.teamId 
              FROM p_league_teams lt 
              WHERE lt.league.leagueId = :leagueId
          )
          AND ts.isDeleted = false
          ORDER BY ts.currentRank ASC
      """)
  Page<TeamScore> findByLeagueIdAndTeamIdInLeagueTeams(@Param("leagueId") UUID leagueId,
      Pageable pageable);

}
