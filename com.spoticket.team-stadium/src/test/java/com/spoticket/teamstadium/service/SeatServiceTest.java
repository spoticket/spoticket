package com.spoticket.teamstadium.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spoticket.teamstadium.application.dto.request.SeatCreateRequest;
import com.spoticket.teamstadium.application.service.SeatService;
import com.spoticket.teamstadium.application.service.StadiumService;
import com.spoticket.teamstadium.domain.model.Seat;
import com.spoticket.teamstadium.domain.model.Stadium;
import com.spoticket.teamstadium.domain.repository.SeatRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.factory.SeatTestFactory;
import com.spoticket.teamstadium.factory.StadiumTestFactory;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import com.spoticket.teamstadium.infrastructure.repository.jpa.JpaStadiumRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

  @Mock
  private SeatRepository seatRepository;

  @InjectMocks
  private SeatService seatService;

  @Mock
  private StadiumService stadiumService;

  @Mock
  private JpaStadiumRepository stadiumRepository;

  // 좌석 정보 등록 테스트
  @Test
  void createSeat_WhenSeatIsDuplicate() {
    // Given
    String section = "tempSection";
    UUID gameId = UUID.randomUUID();
    UUID stadiumId = UUID.randomUUID();
    SeatCreateRequest request = new SeatCreateRequest(
        gameId,
        section,
        Long.parseLong("123"),
        100000
    );

    when(seatRepository.findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
        section,
        gameId,
        stadiumId
    )).thenReturn(Optional.of(mock(Seat.class)));

    // When, Then
    assertThatThrownBy(() -> seatService.createSeat(stadiumId, request))
        .isInstanceOf(BusinessException.class)
        .hasMessage(ErrorCode.DUPLICATE_SEAT_NAME.getMessage());

    verify(seatRepository, times(1))
        .findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
            section,
            gameId,
            stadiumId
        );
  }

  @Test
  void createSeat_success() {
    // Given
    String section = "tempSection";
    UUID gameId = UUID.randomUUID();
    UUID stadiumId = UUID.randomUUID();
    UUID seatId = UUID.randomUUID();

    SeatCreateRequest request = new SeatCreateRequest(
        gameId,
        section,
        Long.parseLong("123"),
        100000
    );

    Stadium stadium = StadiumTestFactory.createWithId(
        stadiumId,
        "test stadium",
        "sample address",
        new GeometryFactory().createPoint(new Coordinate(45.678, 12.345)),
        "https://seatImage.sample.com",
        "sample description"
    );

    Seat mockSeat = SeatTestFactory.createWithId(
        seatId,
        gameId,
        section,
        request.quantity(),
        request.price(),
        stadium
    );

    // Mock 설정
    when(seatRepository.findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
        section,
        gameId,
        stadiumId
    )).thenReturn(Optional.empty());

    when(stadiumService.getStadiumById(stadiumId)).thenReturn(stadium);
    when(seatRepository.save(any(Seat.class))).thenReturn(mockSeat);

    // When
    ApiResponse<Map<String, UUID>> response = seatService.createSeat(stadiumId, request);

    // Then
    verify(seatRepository, times(1))
        .findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
            section,
            gameId,
            stadiumId
        );
    verify(stadiumService, times(1)).getStadiumById(stadiumId);
    verify(seatRepository, times(1)).save(any(Seat.class));

    assertNotNull(response);
    assertEquals(200, response.code());
    assertEquals("등록 완료", response.msg());
    assertNotNull(response.data().get("seatId"));
    assertEquals(seatId, response.data().get("seatId"));
  }

  @Test
  void createSeat_ShouldThrowBusinessException_WhenDuplicateSeatExists() {
    // Given
    UUID stadiumId = UUID.randomUUID();
    UUID gameId = UUID.randomUUID();
    String section = "S ";
    Seat existingSeat = Seat.builder()
        .gameId(gameId)
        .section(section)
        .stadium(Stadium.builder().stadiumId(stadiumId).build())
        .build();

    when(seatRepository.findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
        section, gameId, stadiumId)).thenReturn(Optional.of(existingSeat));

    SeatCreateRequest request = new SeatCreateRequest(gameId, section, 100L, 5000);

    // When & Then
    BusinessException ex = assertThrows(BusinessException.class,
        () -> seatService.createSeat(stadiumId, request));
    assertEquals(ErrorCode.DUPLICATE_SEAT_NAME.getCode(), ex.getCode());
    verify(seatRepository, times(1))
        .findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(section, gameId, stadiumId);
  }

}
