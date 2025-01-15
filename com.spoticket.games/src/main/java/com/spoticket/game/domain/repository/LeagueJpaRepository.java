package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.Sport;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueJpaRepository extends JpaRepository<League, UUID> {

  Optional<League> findByLeagueIdAndIsDeletedFalse(UUID leagueId);

  @Query("SELECT l FROM p_leagues l " +
      "WHERE REPLACE(l.name, ' ', '') = REPLACE(:name, ' ', '') " +
      "AND l.season = :season " +
      "AND l.isDeleted = FALSE")
  Optional<League> findByNameAndSeasonAndIsDeletedFalse(
      @Param("name") String name,
      @Param("season") Integer season
  );

  Page<League> findAllBySportAndIsDeletedFalse(Sport sport, Pageable pageable);

  Page<League> findAllByIsDeletedFalse(Pageable pageable);
}
