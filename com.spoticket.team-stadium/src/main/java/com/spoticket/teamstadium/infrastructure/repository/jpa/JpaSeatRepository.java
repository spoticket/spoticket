package com.spoticket.teamstadium.infrastructure.repository.jpa;


import com.spoticket.teamstadium.domain.model.Seat;
import com.spoticket.teamstadium.domain.repository.SeatRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSeatRepository extends SeatRepository, JpaRepository<Seat, UUID> {

  Optional<Seat> findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
      @Param("section") String section,
      @Param("gameId") UUID gameId,
      @Param("stadiumId") UUID stadiumId);
}
