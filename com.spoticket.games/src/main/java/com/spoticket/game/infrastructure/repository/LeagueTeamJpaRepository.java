package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.LeagueTeam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueTeamJpaRepository extends JpaRepository<LeagueTeam, UUID> {

  @Query("SELECT lt FROM p_league_teams lt "
      + "WHERE lt.teamId = :teamId "
      + "AND lt.league.leagueId = :leagueId "
      + "AND lt.isDeleted = false")
  Optional<LeagueTeam> findByTeamIdAndLeagueIdAndIsDeletedFalse(
      @Param("teamId") UUID teamId,
      @Param("leagueId") UUID leagueId);

  @Query("SELECT lt FROM p_league_teams lt "
      + "WHERE lt.league.leagueId = :leagueId "
      + "AND lt.isDeleted = false")
  List<LeagueTeam> findAllByLeagueIdAndIsDeletedFalse(
      @Param("leagueId") UUID leagueId);
}
