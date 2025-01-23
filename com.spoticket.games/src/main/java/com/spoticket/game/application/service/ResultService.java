package com.spoticket.game.application.service;

import static com.spoticket.game.application.dto.response.UserRoleEnum.ROLE_ADMIN;
import static com.spoticket.game.application.dto.response.UserRoleEnum.ROLE_MASTER;

import com.spoticket.game.application.dto.request.CreateResultRequest;
import com.spoticket.game.application.dto.request.UpdateLeagueGameRequest;
import com.spoticket.game.application.dto.response.GenericPagedModel;
import com.spoticket.game.application.dto.response.ReadLeagueGameListResponse;
import com.spoticket.game.application.dto.response.ReadResultResponse;
import com.spoticket.game.common.exception.CustomException;
import com.spoticket.game.common.util.ApiResponse;
import com.spoticket.game.common.util.RequestUtils;
import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.LeagueGame;
import com.spoticket.game.infrastructure.repository.GameJpaRepository;
import com.spoticket.game.infrastructure.repository.ResultJpaRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultService {

  private final ResultJpaRepository resultJpaRepository;
  private final LeagueService leagueService;
  private final GameJpaRepository gameJpaRepository;

  public ApiResponse<Map<String, UUID>> createResult(
      CreateResultRequest request) {
    validateUserHasAdminOrMasterRole();
    validateLeagueGameNotDuplicated(request.leagueId(), request.gameId());
    League league = leagueService.findById(request.leagueId());
    Optional<Game> game = gameJpaRepository.findById(request.gameId());
    if (game.isEmpty()) {
      throw new CustomException(404, "해당하는 경기가 없습니다");
    }

    LeagueGame lg = LeagueGame.create(
        game.get(), league, request.homeScore(), request.awayScore());
    LeagueGame savedLg = resultJpaRepository.save(lg);
    Map<String, UUID> response = Map.of("LeagueGameId", savedLg.getLeagueGameId());
    return new ApiResponse<>(200, "등록 완료", response);
  }

  public ApiResponse<ReadResultResponse> readResult(UUID leagueGameId) {
    LeagueGame lg = findById(leagueGameId);
    ReadResultResponse response = ReadResultResponse.from(lg, lg.getLeague(), lg.getGame());
    return new ApiResponse<>(200, "조회 완료", response);
  }

  public ApiResponse<GenericPagedModel<ReadLeagueGameListResponse>> readResultList(
      UUID league, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<LeagueGame> lgs = resultJpaRepository.findAllByLeagueAndIsDeletedFalse(league, pageable);
    Page<ReadLeagueGameListResponse> response = lgs.map(ReadLeagueGameListResponse::from);
    return new ApiResponse<>(200, "조회 완료", GenericPagedModel.of(response));
  }

  public ApiResponse<Void> updateResult(UUID leagueGameId, UpdateLeagueGameRequest request) {
    validateUserHasAdminOrMasterRole();
    LeagueGame lg = findById(leagueGameId);
    lg.update(request.homeScore(), request.awayScore());
    resultJpaRepository.save(lg);
    return new ApiResponse<>(200, "수정 완료", null);
  }

  public ApiResponse<Void> deleteResut(UUID leagueGameId) {
    validateUserHasAdminOrMasterRole();
    LeagueGame lg = findById(leagueGameId);
    lg.delete(RequestUtils.getCurrentUserId());
    resultJpaRepository.save(lg);
    return new ApiResponse<>(200, "삭제 완료", null);
  }

  public LeagueGame findById(UUID leagueGameId) {
    return resultJpaRepository.findByLeagueGameIdAndIsDeletedFalse(leagueGameId)
        .orElseThrow(() -> new CustomException(404, "해당하는 경기결과가 없습니다"));
  }

  private void validateLeagueGameNotDuplicated(UUID leagueId, UUID gameId) {
    if (resultJpaRepository.findByGameIdAndLeagueIdAndIsDeletedFalse(
        leagueId, gameId).isPresent()
    ) {
      throw new CustomException(400, "이미 결과가 등록된 경기입니다");
    }
  }

  private void validateUserHasAdminOrMasterRole() {
    if (RequestUtils.getCurrentUserRole() != ROLE_MASTER
        && RequestUtils.getCurrentUserRole() != ROLE_ADMIN) {
      throw new CustomException(403, "권한이 없습니다");
    }
  }

}
