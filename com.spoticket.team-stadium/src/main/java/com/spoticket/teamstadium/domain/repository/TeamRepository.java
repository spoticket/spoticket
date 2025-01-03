package com.spoticket.teamstadium.domain.repository;

import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository {

  Team save(Team team);

  Optional<Team> findByNameAndIsDeletedFalse(String name);

  Optional<Team> findByTeamIdAndIsDeletedFalse(UUID teamId);

  Page<Team> findAllByCategoryAndIsDeletedFalse(TeamCategoryEnum category, Pageable pageable);

  Page<Team> findAllByIsDeletedFalse(Pageable pageable);

  @Query("SELECT t FROM P_TEAMS t WHERE t.teamId IN :teamIds AND t.isDeleted = false")
  Page<Team> findAllByTeamIdInAndIsDeletedFalse(
      @Param("teamIds") List<UUID> teamIds, Pageable pageable);
}
