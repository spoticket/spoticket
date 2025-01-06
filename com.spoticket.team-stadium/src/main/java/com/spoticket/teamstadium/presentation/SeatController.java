package com.spoticket.teamstadium.presentation;

import com.spoticket.teamstadium.application.dto.request.SeatCreateRequest;
import com.spoticket.teamstadium.application.service.SeatService;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
}
