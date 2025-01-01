package com.spoticket.teamstadium.infrastructure;


import com.spoticket.teamstadium.domain.Team;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

}
