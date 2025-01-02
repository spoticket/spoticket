package com.spoticket.teamstadium.infrastructure;


import com.spoticket.teamstadium.domain.Seat;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {

}
