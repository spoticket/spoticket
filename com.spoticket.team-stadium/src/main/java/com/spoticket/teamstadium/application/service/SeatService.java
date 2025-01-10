package com.spoticket.teamstadium.application.service;

import static com.spoticket.teamstadium.domain.model.UserRoleEnum.ROLE_ADMIN;
import static com.spoticket.teamstadium.domain.model.UserRoleEnum.ROLE_MASTER;

import com.spoticket.teamstadium.application.dto.request.SeatCreateRequest;
import com.spoticket.teamstadium.application.dto.request.SeatUpdateRequest;
import com.spoticket.teamstadium.application.dto.response.SeatListReadResponse;
import com.spoticket.teamstadium.domain.model.Seat;
import com.spoticket.teamstadium.domain.model.Stadium;
import com.spoticket.teamstadium.domain.repository.SeatRepository;
import com.spoticket.teamstadium.domain.repository.StadiumRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.exception.NotFoundException;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import com.spoticket.teamstadium.global.util.RequestUtils;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatService {

  private final SeatRepository seatRepository;
  private final StadiumRepository stadiumRepository;

  // 좌석 정보 등록
  @Transactional
  @CacheEvict(
      value = "seatListCache",
      key = "'stadium:' + #stadiumId + ':game:' + #request.gameId()"
  )
  public ApiResponse<Map<String, UUID>> createSeat(
      UUID stadiumId,
      SeatCreateRequest request
  ) {
    if (RequestUtils.getCurrentUserRole() != ROLE_MASTER
        && RequestUtils.getCurrentUserRole() != ROLE_ADMIN) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }
    
    if (seatRepository.findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
        request.section(), request.gameId(), stadiumId).isPresent()
    ) {
      throw new BusinessException(ErrorCode.DUPLICATE_SEAT_NAME);
    }

    Stadium stadium = getStadium(stadiumId);

    Seat seat = Seat.create(
        request.gameId(),
        request.section(),
        request.quantity(),
        request.price(),
        stadium
    );

    Seat savedSeat = seatRepository.save(seat);
    Map<String, UUID> response = Map.of("seatId", savedSeat.getSeatId());
    return new ApiResponse<>(200, "등록 완료", response);
  }

  // 좌석 목록 조회
  @Cacheable(
      value = "seatListCache",
      key = "'stadium:' + #stadiumId + ':game:' + #gameId",
      unless = "#result == null || #result.data == null || #result.data.isEmpty()" // 결과 조건
  )
  public ApiResponse<List<SeatListReadResponse>> getSeatList(UUID stadiumId, UUID gameId) {

    getStadium(stadiumId);

    List<Seat> seats = seatRepository.findAllByGameIdAndStadium_StadiumIdAndIsDeletedFalse(
        stadiumId, gameId
    );
    if (seats.isEmpty()) {
      return new ApiResponse<>(200, "해당 경기장, 경기에 대한 좌석정보가 없습니다", null);
    }

    // 조회 결과 생성
    List<SeatListReadResponse> response = seats.stream()
        .map(SeatListReadResponse::from)
        .toList();

    return new ApiResponse<>(200, "조회 완료", response);
  }

  // 좌석 정보 수정
  @Transactional
  @CacheEvict(
      value = "seatListCache",
      key = "'stadium:' + #stadiumId + ':game:' + #gameId"
  )
  public ApiResponse<Void> updateSeat(
      UUID seatId,
      SeatUpdateRequest request
  ) {
    if (RequestUtils.getCurrentUserRole() != ROLE_MASTER
        && RequestUtils.getCurrentUserRole() != ROLE_ADMIN) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    Seat seat = getSeatById(seatId);
    UUID stadiumId = seat.getStadium().getStadiumId();
    UUID gameId = seat.getGameId();
    if (seatRepository.findBySectionAndGameIdAndStadium_StadiumIdAndIsDeletedFalse(
            request.section(), gameId, stadiumId)
        .isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATE_SEAT_NAME);
    }

    seat.update(request.section(), request.quantity(), request.price());
    seatRepository.save(seat);

    return new ApiResponse<>(200, "수정 완료", null);
  }

  // 좌석 정보 삭제
  @Transactional
  public ApiResponse<Void> deleteSeat(UUID seatId) {

    if (RequestUtils.getCurrentUserRole() != ROLE_MASTER
        && RequestUtils.getCurrentUserRole() != ROLE_ADMIN) {
      throw new BusinessException(ErrorCode.FORBIDDEN);
    }

    Seat seat = getSeatById(seatId);
    seat.deleteBase();
    seatRepository.save(seat);

    return new ApiResponse<>(200, "삭제 완료", null);
  }

  public void deleteSeatList(List<Seat> seats) {
    for (Seat seat : seats) {
      seat.deleteBase();
    }
  }

  public Seat getSeatById(UUID seatId) {
    return seatRepository.findBySeatIdAndIsDeletedFalse(seatId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.SEAT_NOT_FOUND));
  }

  private Stadium getStadium(UUID stadiumId) {
    Stadium stadium = stadiumRepository.findByStadiumIdAndIsDeletedFalse(stadiumId)
        .orElse(null);
    if (stadium == null) {
      throw new BusinessException(ErrorCode.STADIUM_NOT_FOUND);
    }
    return stadium;
  }
}
