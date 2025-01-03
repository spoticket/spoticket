package com.spoticket.teamstadium.application.service;

import com.spoticket.teamstadium.application.dto.request.StadiumCreateRequest;
import com.spoticket.teamstadium.domain.model.Stadium;
import com.spoticket.teamstadium.domain.repository.StadiumRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StadiumService {

  private final StadiumRepository stadiumRepository;

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
}
