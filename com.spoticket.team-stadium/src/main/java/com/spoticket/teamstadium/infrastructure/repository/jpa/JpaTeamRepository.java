package com.spoticket.teamstadium.infrastructure.repository.jpa;


import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.repository.TeamRepository;
import com.spoticket.teamstadium.infrastructure.repository.queryDsl.TeamRepositoryCustom;
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
public interface JpaTeamRepository extends TeamRepository, JpaRepository<Team, UUID>,
    TeamRepositoryCustom {

  @Query("SELECT t FROM p_teams t WHERE REPLACE(t.name, ' ', '') = REPLACE(:name, ' ', '') AND t.isDeleted = false")
  Optional<Team> findByNameAndIsDeletedFalse(@Param("name") String name);

  @Query("SELECT t FROM p_teams t WHERE t.teamId IN :teamIds AND t.isDeleted = false")
  Page<Team> findAllByTeamIdInAndIsDeletedFalse(
      @Param("teamIds") List<UUID> teamIds,
      Pageable pageable
  );
}