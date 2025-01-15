package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.League;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueJpaRepository extends JpaRepository<League, UUID> {

  Optional<League> findByLeagueIdAndIsDeletedFalse(UUID leagueId);
}
