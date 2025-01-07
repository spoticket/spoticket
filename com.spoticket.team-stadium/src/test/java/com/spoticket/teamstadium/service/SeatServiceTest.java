package com.spoticket.teamstadium.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spoticket.teamstadium.application.dto.request.SeatCreateRequest;
import com.spoticket.teamstadium.application.dto.request.SeatUpdateRequest;
import com.spoticket.teamstadium.application.service.SeatService;
import com.spoticket.teamstadium.application.service.StadiumService;
import com.spoticket.teamstadium.domain.model.Seat;
import com.spoticket.teamstadium.domain.model.Stadium;
import com.spoticket.teamstadium.domain.repository.SeatRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.exception.NotFoundException;
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

  // 수정
  @Test
  void updateSeat_success() {
    // Given
    UUID seatId = UUID.randomUUID();
    UUID stadiumId = UUID.randomUUID();
    UUID gameId = UUID.randomUUID();
    String section = "A";

    Seat existingSeat = Seat.builder()
        .seatId(seatId)
        .section("B")
        .quantity(50L)
        .price(10000)
        .stadium(Stadium.builder().stadiumId(stadiumId).build())
        .gameId(gameId)
        .build();

    SeatUpdateRequest request = new SeatUpdateRequest(section, 100L, 20000);

    when(seatRepository.findBySeatIdAndIsDeletedFalse(seatId)).thenReturn(
        Optional.of(existingSeat));
    when(seatRepository.findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
        section, gameId, stadiumId)).thenReturn(Optional.empty());
    when(seatRepository.save(any(Seat.class))).thenReturn(existingSeat);

    // When
    ApiResponse<Void> response = seatService.updateSeat(seatId, request);

    // Then
    verify(seatRepository, times(1)).findBySeatIdAndIsDeletedFalse(seatId);
    verify(seatRepository, times(1)).findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
        section, gameId, stadiumId);
    verify(seatRepository, times(1)).save(existingSeat);

    assertNotNull(response);
    assertEquals(200, response.code());
    assertEquals("수정 완료", response.msg());
  }

  @Test
  void updateSeat_WhenDuplicateSectionExists() {
    // Given
    UUID seatId = UUID.randomUUID();
    UUID stadiumId = UUID.randomUUID();
    UUID gameId = UUID.randomUUID();
    String duplicateSection = "A";

    Seat existingSeat = Seat.builder()
        .seatId(seatId)
        .section("B")
        .quantity(50L)
        .price(10000)
        .stadium(Stadium.builder().stadiumId(stadiumId).build())
        .gameId(gameId)
        .build();

    Seat duplicateSeat = Seat.builder()
        .seatId(UUID.randomUUID())
        .section(duplicateSection)
        .quantity(30L)
        .price(8000)
        .stadium(Stadium.builder().stadiumId(stadiumId).build())
        .gameId(gameId)
        .build();

    SeatUpdateRequest request = new SeatUpdateRequest(duplicateSection, 100L, 20000);

    when(seatRepository.findBySeatIdAndIsDeletedFalse(seatId)).thenReturn(
        Optional.of(existingSeat));
    when(seatRepository.findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
        duplicateSection, gameId, stadiumId)).thenReturn(Optional.of(duplicateSeat));

    // When, Then
    BusinessException exception = assertThrows(BusinessException.class,
        () -> seatService.updateSeat(seatId, request));

    assertEquals(ErrorCode.DUPLICATE_SEAT_NAME.getCode(), exception.getCode());

    verify(seatRepository, times(1)).findBySeatIdAndIsDeletedFalse(seatId);
    verify(seatRepository, times(1)).findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
        duplicateSection, gameId, stadiumId);
    verify(seatRepository, never()).save(any(Seat.class));
  }

  // 삭제
  @Test
  void deleteSeat_success() {
    // Given
    UUID seatId = UUID.randomUUID();

    Stadium stadium = Stadium.builder()
        .stadiumId(UUID.randomUUID())
        .name("Test Stadium")
        .build();

    Seat seat = Seat.builder()
        .seatId(seatId)
        .stadium(stadium)
        .section("A")
        .quantity(100L)
        .price(50000)
        .build();

    when(seatRepository.findBySeatIdAndIsDeletedFalse(seatId)).thenReturn(Optional.of(seat));
    when(seatRepository.save(any(Seat.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // When
    ApiResponse<Void> response = seatService.deleteSeat(seatId);

    // Then
    assertNotNull(response);
    assertEquals(200, response.code());
    assertEquals("삭제 완료", response.msg());

    verify(seatRepository, times(1)).findBySeatIdAndIsDeletedFalse(seatId);
    verify(seatRepository, times(1)).save(any(Seat.class));
    assertTrue(seat.isDeleted());
  }

  @Test
  void deleteSeat_WhenSeatNotFound() {
    // Given
    UUID seatId = UUID.randomUUID();
    when(seatRepository.findBySeatIdAndIsDeletedFalse(seatId)).thenReturn(Optional.empty());

    // When, Then
    assertThrows(NotFoundException.class, () -> seatService.deleteSeat(seatId));
    verify(seatRepository, times(1)).findBySeatIdAndIsDeletedFalse(seatId);
    verify(seatRepository, never()).save(any(Seat.class));
  }
}
