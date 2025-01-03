package com.spoticket.teamstadium.infrastructure.repository.jpa;


import com.spoticket.teamstadium.domain.model.FavTeam;
import com.spoticket.teamstadium.domain.repository.FavTeamRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFavTeamRepository extends FavTeamRepository, JpaRepository<FavTeam, UUID> {

  @Modifying
  @Query("DELETE FROM p_fav_teams f WHERE f IN :favTeams")
  void deleteAll(@Param("favTeams") List<FavTeam> favTeams);

  @Query("SELECT f FROM p_fav_teams f WHERE f.team.teamId = :teamId")
  List<FavTeam> findAllByTeamId(@Param("teamId") UUID teamId);

  @Query("SELECT f FROM p_fav_teams f WHERE f.userId = :userId")
  Page<FavTeam> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);
}
