package com.spoticket.game.application.service;

import static com.spoticket.game.global.entity.UserRoleEnum.ROLE_ADMIN;
import static com.spoticket.game.global.entity.UserRoleEnum.ROLE_MASTER;

import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.Sport;
import com.spoticket.game.domain.repository.LeagueJpaRepository;
import com.spoticket.game.dto.request.CreateLeagueRequest;
import com.spoticket.game.dto.response.GenericPagedModel;
import com.spoticket.game.dto.response.ReadLeagueListResponse;
import com.spoticket.game.dto.response.ReadLeagueResponse;
import com.spoticket.game.global.exception.CustomException;
import com.spoticket.game.global.util.RequestUtils;
import com.spoticket.game.global.util.ResponseUtils.DataResponse;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeagueService {

  private final LeagueJpaRepository leagueJpaRepository;

  // 리그 정보 등록
  public DataResponse<Map<String, UUID>> createLeague(
      CreateLeagueRequest request
  ) {
    if (RequestUtils.getCurrentUserRole() != ROLE_MASTER
        && RequestUtils.getCurrentUserRole() != ROLE_ADMIN) {
      throw new CustomException(403, "권한이 없습니다");
    }
    if (leagueJpaRepository.findByNameAndSeasonAndIsDeletedFalse(
        request.name(), request.season()).isPresent()) {
      throw new CustomException(400, "이미 등록되어있는 리그입니다");
    }

    League league = League.create(
        request.name(),
        request.sport(),
        request.season(),
        request.startAt(),
        request.endAt()
    );
    League savedLeague = leagueJpaRepository.save(league);
    Map<String, UUID> response = Map.of("leagueId", savedLeague.getLeagueId());
    return new DataResponse<>(200, "등록 완료", response);
  }

  // 단일 리그 조회
  public DataResponse<ReadLeagueResponse> readLeague(UUID leagueId) {
    League league = findById(leagueId);
    ReadLeagueResponse response = ReadLeagueResponse.from(league);
    return new DataResponse<>(200, "조회 완료", response);
  }

  // 리그 목록 조회
  public DataResponse<GenericPagedModel<ReadLeagueListResponse>> readLeagueList(
      Sport sport,
      int page,
      int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<League> leagues;
    if (sport != null) {
      leagues = leagueJpaRepository.findAllBySportAndIsDeletedFalse(sport, pageable);
    } else {
      leagues = leagueJpaRepository.findAllByIsDeletedFalse(pageable);
    }
    Page<ReadLeagueListResponse> response = leagues.map(ReadLeagueListResponse::from);
    return new DataResponse<>(200, "조회 완료", GenericPagedModel.of(response));
  }

  public League findById(UUID leagueId) {
    return leagueJpaRepository.findByLeagueIdAndIsDeletedFalse(leagueId)
        .orElseThrow(() -> new CustomException(400, "해당하는 리그가 없습니다"));
  }

}
