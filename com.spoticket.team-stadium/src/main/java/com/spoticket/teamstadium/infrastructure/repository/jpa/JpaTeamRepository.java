package com.spoticket.teamstadium.infrastructure.repository.jpa;


import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.repository.TeamRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTeamRepository extends TeamRepository, JpaRepository<Team, UUID> {

  @Override
  Optional<Team> findByName(String name);

}
