package com.spoticket.teamstadium.presentation;

import com.spoticket.teamstadium.application.dto.request.StadiumCreateRequest;
import com.spoticket.teamstadium.application.dto.request.StadiumUpdateRequest;
import com.spoticket.teamstadium.application.dto.response.StadiumListReadResponse;
import com.spoticket.teamstadium.application.dto.response.StadiumReadResponse;
import com.spoticket.teamstadium.application.service.StadiumService;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import com.spoticket.teamstadium.global.dto.PaginatedResponse;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/stadiums")
@RequiredArgsConstructor
public class StadiumController {

  private final StadiumService stadiumService;

  // 경기장 정보 등록
  @PostMapping
  public ApiResponse<Map<String, UUID>> createStadium(
      @Valid @RequestBody StadiumCreateRequest request
  ) {
    return stadiumService.createStadium(request);
  }

  // 경기장 단일 조회
  @GetMapping("/{stadiumId}")
  public ApiResponse<StadiumReadResponse> getStadiumInfo(
      @PathVariable UUID stadiumId
  ) {
    return stadiumService.getStadiumInfo(stadiumId);
  }

  // 경기장 목록 조회
  @GetMapping
  public ApiResponse<PaginatedResponse<StadiumListReadResponse>> getStadiums(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    PaginatedResponse<StadiumListReadResponse> response = stadiumService
        .getStadiums(page, size);
    return new ApiResponse<>(200, "조회 완료", response);
  }

  // 경기장 정보 수정
  @PatchMapping("/{stadiumId}")
  public ApiResponse<Void> updateStadium(
      @PathVariable UUID stadiumId,
      @Valid @RequestBody StadiumUpdateRequest request
  ) {
    return stadiumService.updateStadium(stadiumId, request);
  }

  // 경기장 삭제
  @DeleteMapping("/{stadiumId}")
  public ApiResponse<Void> deleteStadium(
      @PathVariable UUID stadiumId
  ) {
    return stadiumService.deleteStadium(stadiumId);
  }

}
