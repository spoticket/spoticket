package com.spoticket.teamstadium.domain.repository;

import com.spoticket.teamstadium.domain.model.Team;
import java.util.Optional;
import java.util.UUID;

public interface TeamRepository {

  Team save(Team team);

  Optional<Team> findByNameAndIsDeletedFalse(String name);

  Optional<Team> findByTeamIdAndIsDeletedFalse(UUID teamId);
}
