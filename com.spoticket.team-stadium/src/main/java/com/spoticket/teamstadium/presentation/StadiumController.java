package com.spoticket.teamstadium.presentation;

import com.spoticket.teamstadium.application.dto.request.StadiumCreateRequest;
import com.spoticket.teamstadium.application.service.StadiumService;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stadiums")
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

}
