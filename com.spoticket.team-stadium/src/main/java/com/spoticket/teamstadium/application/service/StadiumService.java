package com.spoticket.teamstadium.application.service;

import com.spoticket.teamstadium.application.dto.request.StadiumCreateRequest;
import com.spoticket.teamstadium.application.dto.request.StadiumUpdateRequest;
import com.spoticket.teamstadium.application.dto.response.GameReadResponse;
import com.spoticket.teamstadium.application.dto.response.StadiumInfoResponse;
import com.spoticket.teamstadium.application.dto.response.StadiumListReadResponse;
import com.spoticket.teamstadium.application.dto.response.StadiumReadResponse;
import com.spoticket.teamstadium.domain.model.Stadium;
import com.spoticket.teamstadium.domain.repository.StadiumRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.exception.NotFoundException;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import com.spoticket.teamstadium.global.dto.PaginatedResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StadiumService {

  private final StadiumRepository stadiumRepository;

  @Transactional
  public ApiResponse<Map<String, UUID>> createStadium(StadiumCreateRequest request) {
    // 요청자 권한 체크

    // 경기장 이름 중복 체크
    if (stadiumRepository.findByNameAndIsDeletedFalse(request.name()).isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATE_STADIUM_NAME);
    }

    GeometryFactory geometryFactory = new GeometryFactory();
    Point latLng = geometryFactory.createPoint(new Coordinate(request.lng(), request.lat()));
    Stadium stadium = Stadium.create(
        request.name(),
        request.address(),
        latLng,
        request.seatImage(),
        request.description()
    );

    Stadium savedStadium = stadiumRepository.save(stadium);

    Map<String, UUID> response = Map.of("stadiumId", savedStadium.getStadiumId());
    return new ApiResponse<>(200, "등록 완료", response);
  }

  // 경기장 단일 조회
  public ApiResponse<StadiumReadResponse> getStadiumInfo(UUID stadiumId) {

    Stadium stadium = getStadiumById(stadiumId);
    StadiumInfoResponse stadiumInfo = StadiumInfoResponse.from(stadium);
    // 경기장 관련 게임 정보 조회 메서드 호출 필요
    List<GameReadResponse> games = null;
    StadiumReadResponse response = new StadiumReadResponse(stadiumInfo, games);
    return new ApiResponse<>(200, "조회 완료", response);
  }

  // 경기장 목록 조회
  public PaginatedResponse<StadiumListReadResponse> getStadiums(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Stadium> stadiums = stadiumRepository.findAllByIsDeletedFalse(pageable);

    Page<StadiumListReadResponse> response = stadiums.map(StadiumListReadResponse::from);
    return PaginatedResponse.of(response);
  }

  // 경기장 정보 수정
  @Transactional
  public ApiResponse<Void> updateStadium(
      UUID stadiumId,
      StadiumUpdateRequest request
  ) {
    // 요청자 권한 체크

    Stadium stadium = getStadiumById(stadiumId);
    if (request.name() != null &&
        stadiumRepository.findByNameAndIsDeletedFalse(request.name())
            .filter(existingStadium -> !existingStadium.getStadiumId().equals(stadiumId))
            .isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATE_STADIUM_NAME);
    }
    GeometryFactory geometryFactory = new GeometryFactory();
    Point latLng = geometryFactory.createPoint(new Coordinate(request.lng(), request.lat()));

    stadium.update(
        request.name(), request.address(),
        request.seatImage(), request.description(),
        latLng
    );
    stadiumRepository.save(stadium);
    return new ApiResponse<>(200, "수정 완료", null);
  }

  public Stadium getStadiumById(UUID stadiumId) {
    return stadiumRepository.findByStadiumIdAndIsDeletedFalse(stadiumId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.STADIUM_NOT_FOUND));

  }


}
