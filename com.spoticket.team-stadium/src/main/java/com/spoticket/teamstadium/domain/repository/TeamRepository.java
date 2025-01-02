package com.spoticket.teamstadium.domain.repository;

import com.spoticket.teamstadium.domain.model.Team;
import java.util.Optional;

public interface TeamRepository {

  Team save(Team team);

  Optional<Team> findByName(String name);
}
