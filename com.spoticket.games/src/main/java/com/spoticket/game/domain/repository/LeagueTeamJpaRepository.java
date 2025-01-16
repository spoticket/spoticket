package com.spoticket.game.domain.repository;

import com.spoticket.game.domain.model.LeagueTeam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueTeamJpaRepository extends JpaRepository<LeagueTeam, UUID> {

  Optional<LeagueTeam> findByTeamIdAndLeagueIdAndIsDeletedFalse(UUID teamId, UUID leagueId);

  List<LeagueTeam> findAllByLeagueIdAndIsDeletedFalse(UUID leagueId);
}
