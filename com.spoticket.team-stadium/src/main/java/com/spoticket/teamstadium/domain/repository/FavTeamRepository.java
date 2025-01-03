package com.spoticket.teamstadium.domain.repository;

import com.spoticket.teamstadium.domain.model.FavTeam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavTeamRepository {

  FavTeam save(FavTeam favTeam);

  void delete(FavTeam favTeam);

  void deleteAll(List<FavTeam> favTeams);

  long countByTeam_TeamId(UUID teamId);

  Page<FavTeam> findAllByUserId(UUID userId, Pageable pageable);

  Optional<FavTeam> findByUserIdAndTeam_TeamId(UUID userId, UUID teamId);

  List<FavTeam> findAllByTeamId(UUID teamId);

}
