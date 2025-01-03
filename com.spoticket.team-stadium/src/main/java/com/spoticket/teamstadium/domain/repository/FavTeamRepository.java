package com.spoticket.teamstadium.domain.repository;

import com.spoticket.teamstadium.domain.model.FavTeam;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavTeamRepository {

  FavTeam save(FavTeam favTeam);

  void delete(FavTeam favTeam);

  long countByTeam_TeamId(UUID teamId);

  @Query("SELECT f FROM p_fav_teams f WHERE f.userId = :userId")
  Page<FavTeam> findAllByUserId(
      @Param("userId") UUID userId, Pageable pageable);

  Optional<FavTeam> findByUserIdAndTeam_TeamId(UUID userId, UUID teamId);


}
