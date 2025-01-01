package com.spoticket.teamstadium.infrastructure;


import com.spoticket.teamstadium.domain.FavTeam;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavTeamRepository extends JpaRepository<FavTeam, UUID> {

}
