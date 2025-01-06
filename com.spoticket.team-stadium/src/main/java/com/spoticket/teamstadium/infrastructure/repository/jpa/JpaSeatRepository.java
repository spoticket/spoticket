package com.spoticket.teamstadium.infrastructure.repository.jpa;


import com.spoticket.teamstadium.domain.model.Seat;
import com.spoticket.teamstadium.domain.repository.SeatRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSeatRepository extends SeatRepository, JpaRepository<Seat, UUID> {

  @Query("SELECT s FROM p_seats s JOIN FETCH s.stadium WHERE " +
      "REPLACE(s.section, ' ', '') = REPLACE(:section, ' ', '') " +
      "AND s.gameId = :gameId " +
      "AND s.stadium.stadiumId = :stadiumId " +
      "AND s.isDeleted = false")
  Optional<Seat> findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
      @Param("section") String section,
      @Param("gameId") UUID gameId,
      @Param("stadiumId") UUID stadiumId);

  @Query("SELECT s FROM p_seats s WHERE s.gameId = :gameId " +
      "AND s.stadium.stadiumId = :stadiumId AND s.isDeleted = false")
  List<Seat> findAllByGameIdAndStadium_StadiumIdAndIsDeletedFalse(
      @Param("stadiumId") UUID stadiumId,
      @Param("gameId") UUID gameId
  );
}
