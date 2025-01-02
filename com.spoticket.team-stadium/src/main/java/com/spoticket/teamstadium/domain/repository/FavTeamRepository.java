package com.spoticket.teamstadium.domain.repository;

import java.util.UUID;

public interface FavTeamRepository {

  long countByTeam_TeamId(UUID teamId);
}
