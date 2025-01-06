package com.spoticket.teamstadium.application.service;

import com.spoticket.teamstadium.application.dto.request.SeatCreateRequest;
import com.spoticket.teamstadium.domain.model.Seat;
import com.spoticket.teamstadium.domain.model.Stadium;
import com.spoticket.teamstadium.domain.repository.SeatRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.exception.NotFoundException;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatService {

  private final SeatRepository seatRepository;
  private final StadiumService stadiumService;

  // 좌석 정보 등록
  public ApiResponse<Map<String, UUID>> createSeat(
      UUID stadiumId,
      SeatCreateRequest request
  ) {
    // 요청자 권한 체크

    // 게임 유효성 체크

    if (seatRepository.findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
        request.section(), request.gameId(), stadiumId).isPresent()
    ) {
      throw new BusinessException(ErrorCode.DUPLICATE_SEAT_NAME);
    }

    Stadium stadium = stadiumService.getStadiumById(stadiumId);

    Seat seat = Seat.create(
        request.gameId(),
        request.section(),
        request.quantity(),
        request.price(),
        stadium
    );

    Seat savedSeat = seatRepository.save(seat);
    Map<String, UUID> response = Map.of("seatId", savedSeat.getSeatId());
    return new ApiResponse<>(200, "등록 완료", response);
  }

  public Seat getSeatById(UUID seatId) {
    return seatRepository.findBySeatIdAndIsDeletedFalse(seatId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
  }
}
