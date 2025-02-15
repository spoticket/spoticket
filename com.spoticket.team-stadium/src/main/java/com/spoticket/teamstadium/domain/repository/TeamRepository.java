package com.spoticket.teamstadium.domain.repository;

import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamRepository {


  Team save(Team team);

  Optional<Team> findByNameAndIsDeletedFalse(String name);

  Optional<Team> findByTeamIdAndIsDeletedFalse(UUID teamId);

  Page<Team> findAllByCategoryAndIsDeletedFalse(TeamCategoryEnum category, Pageable pageable);

  Page<Team> findAllByIsDeletedFalse(Pageable pageable);

  Page<Team> findAllByTeamIdInAndIsDeletedFalse(List<UUID> teamIds, Pageable pageable);

  List<Team> searchByKeyword(String keyword);
}
