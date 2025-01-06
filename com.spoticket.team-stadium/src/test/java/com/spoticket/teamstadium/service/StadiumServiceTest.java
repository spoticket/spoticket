package com.spoticket.teamstadium.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spoticket.teamstadium.application.dto.request.StadiumCreateRequest;
import com.spoticket.teamstadium.application.dto.request.StadiumUpdateRequest;
import com.spoticket.teamstadium.application.dto.response.StadiumReadResponse;
import com.spoticket.teamstadium.application.service.StadiumService;
import com.spoticket.teamstadium.domain.model.Stadium;
import com.spoticket.teamstadium.domain.repository.StadiumRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.exception.NotFoundException;
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

  // 경기장 단일 조회
  @Test
  void getStadiumInfo_FeignClientIsExcluded() {
    // Given
    UUID stadiumId = UUID.randomUUID();
    Point latLng = new GeometryFactory().createPoint(new Coordinate(12.345, 67.89));
    Stadium mockStadium = Stadium.builder()
        .stadiumId(stadiumId)
        .name("test stadium")
        .address("test address")
        .latLng(latLng)
        .seatImage("http://sampleImage.com")
        .description("test description")
        .build();

    when(stadiumRepository.findByStadiumIdAndIsDeletedFalse(stadiumId)).thenReturn(
        Optional.of(mockStadium));

    // When
    ApiResponse<StadiumReadResponse> response = stadiumService.getStadiumInfo(stadiumId);

    // Then
    assertNotNull(response);
    assertEquals(200, response.code());

    StadiumReadResponse res = response.data();
    assertEquals(stadiumId, res.stadium().stadiumId());
    assertEquals("test stadium", res.stadium().name());
    assertEquals("test address", res.stadium().address());
  }

  // 팀 목록 조회
  @Test
  void testGetStadiumInfo_Success() {
    // Given
    UUID stadiumId = UUID.randomUUID();
    Stadium stadium = StadiumTestFactory.createWithId(
        stadiumId,
        "test stadium",
        "sample address",
        new GeometryFactory().createPoint(new Coordinate(45.678, 12.345)),
        "https://seatImage.sample.com",
        "sample description"
    );

    when(stadiumRepository.findByStadiumIdAndIsDeletedFalse(stadiumId))
        .thenReturn(Optional.of(stadium));

    // When
    ApiResponse<StadiumReadResponse> response = stadiumService.getStadiumInfo(stadiumId);

    // Then
    verify(stadiumRepository, times(1)).findByStadiumIdAndIsDeletedFalse(stadiumId);

    assertThat(response).isNotNull();
    assertThat(response.code()).isEqualTo(200);
    assertThat(response.msg()).isEqualTo("조회 완료");

    StadiumReadResponse stadiumReadResponse = response.data();
    assertThat(stadiumReadResponse).isNotNull();
    assertThat(stadiumReadResponse.stadium().name()).isEqualTo("test stadium");
    assertThat(stadiumReadResponse.stadium().address()).isEqualTo("sample address");
  }

  @Test
  void testGetStadiumInfo_WhenStadiumDoesNotExist() {
    // Given
    UUID stadiumId = UUID.randomUUID();

    when(stadiumRepository.findByStadiumIdAndIsDeletedFalse(stadiumId))
        .thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> stadiumService.getStadiumInfo(stadiumId))
        .isInstanceOf(NotFoundException.class)
        .hasMessage(ErrorCode.STADIUM_NOT_FOUND.getMessage());

    verify(stadiumRepository, times(1)).findByStadiumIdAndIsDeletedFalse(stadiumId);
  }

  // 경기장 정보 수정
  @Test
  void updateStadium_success() {
    // Given
    UUID stadiumId = UUID.randomUUID();

    Stadium existingStadium = Stadium.builder()
        .stadiumId(stadiumId)
        .name("temp stadium name")
        .address("temp address")
        .latLng(new GeometryFactory().createPoint(new Coordinate(45.678, 12.345)))
        .build();

    StadiumUpdateRequest request = new StadiumUpdateRequest(
        "update name",
        "update address",
        "update seatImage",
        "description",
        11.222,
        22.3333
    );

    when(stadiumRepository.findByStadiumIdAndIsDeletedFalse(stadiumId)).thenReturn(
        Optional.of(existingStadium));
    when(stadiumRepository.findByNameAndIsDeletedFalse(request.name()))
        .thenReturn(Optional.empty());

    // When
    ApiResponse<Void> response = stadiumService.updateStadium(stadiumId, request);

    // Then
    assertEquals(200, response.code());
    assertEquals("수정 완료", response.msg());
    assertEquals("update name", existingStadium.getName());
    assertEquals("update seatImage", existingStadium.getSeatImage());
    verify(stadiumRepository).save(existingStadium);
  }

  @Test
  void updateStadium_WhenStadiumDoesNotExist() {
    // Given
    UUID stadiumId = UUID.randomUUID();
    StadiumUpdateRequest request = new StadiumUpdateRequest(
        "update name",
        "update address",
        "update seatImage",
        "description",
        11.222,
        22.3333
    );

    when(stadiumRepository.findByStadiumIdAndIsDeletedFalse(stadiumId)).thenReturn(
        Optional.empty());

    // Then
    assertThatThrownBy(() -> stadiumService.updateStadium(stadiumId, request))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("해당하는 경기장이 없습니다");

    verify(stadiumRepository, never()).save(any());
  }

  // 경기장 삭제
  @Test
  void deleteStadium_success() {
    // Given
    UUID stadiumId = UUID.randomUUID();
    Stadium stadium = Stadium.builder()
        .stadiumId(stadiumId)
        .name("temp stadium name")
        .address("temp address")
        .latLng(new GeometryFactory().createPoint(new Coordinate(45.678, 12.345)))
        .build();
    when(stadiumRepository.findByStadiumIdAndIsDeletedFalse(stadiumId))
        .thenReturn(Optional.of(stadium));

    // When
    ApiResponse<Void> res = stadiumService.deleteStadium(stadiumId);

    // Then
    assertNotNull(res);
    assertEquals(200, res.code());
    assertEquals("삭제 완료", res.msg());
    assertTrue(stadium.isDeleted());
    verify(stadiumRepository, times(1)).save(stadium);
  }
}


