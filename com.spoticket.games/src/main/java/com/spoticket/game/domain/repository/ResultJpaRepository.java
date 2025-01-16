package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.LeagueGame;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultJpaRepository extends JpaRepository<LeagueGame, UUID> {

  @Query("SELECT lg FROM p_league_games lg " +
      "WHERE lg.league.leagueId = :leagueId " +
      "AND lg.game.gameId = :gameId " +
      "AND lg.isDeleted = false")
  Optional<LeagueGame> findByGameIdAndLeagueIdAndIsDeletedFalse(
      @Param("leagueId") UUID leagueId, @Param("gameId") UUID gameId);

  Optional<LeagueGame> findByLeagueGameIdAndIsDeletedFalse(UUID leagueGameId);

  @Query("SELECT lg FROM p_league_games lg " +
      "WHERE (:league IS NULL OR lg.league.leagueId = :league) " +
      "AND lg.isDeleted = false")
  Page<LeagueGame> findAllByLeagueAndIsDeletedFalse(
      @Param("league") UUID league,
      Pageable pageable);

  @Query("SELECT lg FROM p_league_games lg " +
      "WHERE lg.league.leagueId = :leagueId " +
      "AND lg.createdAt = :date " +
      "AND lg.isDeleted = false")
  List<LeagueGame> findAllByLeagueIdAndDateAndIsDeletedFalse(
      @Param("leagueId") UUID leagueId,
      @Param("date") LocalDate date);
}
