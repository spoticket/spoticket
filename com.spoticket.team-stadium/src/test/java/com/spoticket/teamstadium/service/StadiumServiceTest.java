package com.spoticket.teamstadium.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spoticket.teamstadium.application.dto.request.StadiumCreateRequest;
import com.spoticket.teamstadium.application.service.StadiumService;
import com.spoticket.teamstadium.domain.model.Stadium;
import com.spoticket.teamstadium.domain.repository.StadiumRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.factory.StadiumTestFactory;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class StadiumServiceTest {

  @Mock
  private StadiumRepository stadiumRepository;

  @InjectMocks
  private StadiumService stadiumService;

  // 경기장 생성 테스트
  @Test
  void createStadium_WhenStadiumNameIsDuplicate() {

    // Given
    String stadiumName = "exist name";
    StadiumCreateRequest request = new StadiumCreateRequest(
        stadiumName,
        "sample address",
        12.345,
        45.678,
        "https://seatImage.sample.com",
        "sample description"
    );

    when(stadiumRepository.findByNameAndIsDeletedFalse(stadiumName)).thenReturn(
        Optional.of(mock(Stadium.class)));

    // When Then
    assertThatThrownBy(() -> stadiumService.createStadium(request))
        .isInstanceOf(BusinessException.class)
        .hasMessage(ErrorCode.DUPLICATE_STADIUM_NAME.getMessage());

    verify(stadiumRepository, times(1)).findByNameAndIsDeletedFalse(stadiumName);
  }

  @Test
  void createTeam_WhenStadiumIsCreatedSuccessfully() {

    // Given
    String stadiumName = "new stadium";
    StadiumCreateRequest request = new StadiumCreateRequest(
        stadiumName,
        "sample address",
        12.345,
        45.678,
        "https://seatImage.sample.com",
        "sample description"
    );

    when(stadiumRepository.findByNameAndIsDeletedFalse(stadiumName))
        .thenReturn(Optional.empty());

    UUID stadiumId = UUID.randomUUID();
    Point latLng = new GeometryFactory().createPoint(new Coordinate(request.lng(), request.lat()));

    Stadium mockStadium = StadiumTestFactory.createWithId(
        stadiumId,
        request.name(),
        request.address(),
        latLng,
        request.seatImage(),
        request.description()
    );

    when(stadiumRepository.save(any(Stadium.class))).thenReturn(mockStadium);

    // When
    ApiResponse<Map<String, UUID>> response = stadiumService.createStadium(request);

    // Then
    verify(stadiumRepository, times(1)).findByNameAndIsDeletedFalse(stadiumName);
    verify(stadiumRepository, times(1)).save(any(Stadium.class));

    assertNotNull(response);
    assertEquals(200, response.code());
    assertEquals("등록 완료", response.msg());
    assertNotNull(response.data().get("stadiumId"));
    assertEquals(stadiumId, response.data().get("stadiumId"));
  }
}


