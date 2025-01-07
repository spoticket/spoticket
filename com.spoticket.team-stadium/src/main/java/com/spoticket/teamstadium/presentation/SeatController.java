package com.spoticket.teamstadium.presentation;

import com.spoticket.teamstadium.application.dto.request.SeatCreateRequest;
import com.spoticket.teamstadium.application.dto.request.SeatUpdateRequest;
import com.spoticket.teamstadium.application.dto.response.SeatListReadResponse;
import com.spoticket.teamstadium.application.service.SeatService;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stadiums/seats")
@RequiredArgsConstructor
public class SeatController {

  private final SeatService seatService;

  // 좌석 정보 등록
  @PostMapping("/{stadiumId}")
  public ApiResponse<Map<String, UUID>> createSeat(
      @PathVariable UUID stadiumId,
      @Valid @RequestBody SeatCreateRequest request
  ) {
    return seatService.createSeat(stadiumId, request);
  }

  // 좌석 정보 목록 조회
  @GetMapping("/{stadiumId}/games/{gameId}")
  public ApiResponse<List<SeatListReadResponse>> getSeatList(
      @PathVariable UUID stadiumId,
      @PathVariable UUID gameId
  ) {
    return seatService.getSeatList(stadiumId, gameId);
  }

  // 좌석 정보 수정
  @PatchMapping("/{seatId}")
  public ApiResponse<Void> updateSeat(
      @PathVariable UUID seatId,
      @Valid @RequestBody SeatUpdateRequest request
  ) {
    return seatService.updateSeat(seatId, request);
  }

  // 좌석 정보 삭제
  @DeleteMapping("/{seatId}")
  public ApiResponse<Void> deleteSeat(
      @PathVariable UUID seatId
  ) {
    return seatService.deleteSeat(seatId);
  }

}
