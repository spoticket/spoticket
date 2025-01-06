package com.spoticket.teamstadium.domain.repository;

import com.spoticket.teamstadium.domain.model.Seat;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository {

  Optional<Seat> findBySeatIdAndIsDeletedFalse(UUID seatId);

  Optional<Seat> findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
      String section, UUID gameId, UUID stadiumId);

  Seat save(Seat seat);
}
