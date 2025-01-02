package com.spoticket.teamstadium.infrastructure.repository.jpa;


import com.spoticket.teamstadium.domain.model.FavTeam;
import com.spoticket.teamstadium.domain.repository.FavTeamRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFavTeamRepository extends FavTeamRepository, JpaRepository<FavTeam, UUID> {

}
