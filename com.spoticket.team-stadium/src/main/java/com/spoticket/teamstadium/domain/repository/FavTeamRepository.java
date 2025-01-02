package com.spoticket.teamstadium.domain.repository;

import com.spoticket.teamstadium.domain.model.FavTeam;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavTeamRepository {

  long countByTeam_TeamId(UUID teamId);

  @Query("SELECT f FROM P_FAV_TEAMS f WHERE f.userId = :userId")
  Page<FavTeam> findAllByUserId(
      @Param("userId") UUID userId, Pageable pageable);

}
