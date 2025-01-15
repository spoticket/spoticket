package com.spoticket.game.application.service;

import static com.spoticket.game.global.entity.UserRoleEnum.ROLE_ADMIN;
import static com.spoticket.game.global.entity.UserRoleEnum.ROLE_MASTER;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.LeagueGame;
import com.spoticket.game.domain.repository.GameJpaRepository;
import com.spoticket.game.domain.repository.ResultJpaRepository;
import com.spoticket.game.dto.request.CreateResultRequest;
import com.spoticket.game.dto.request.UpdateLeagueGameRequest;
import com.spoticket.game.dto.response.GenericPagedModel;
import com.spoticket.game.dto.response.ReadLeagueGameListResponse;
import com.spoticket.game.dto.response.ReadResultResponse;
import com.spoticket.game.global.exception.CustomException;
import com.spoticket.game.global.util.RequestUtils;
import com.spoticket.game.global.util.ResponseUtils.DataResponse;
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

  public DataResponse<Map<String, UUID>> createResult(
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
    return new DataResponse<>(200, "등록 완료", response);
  }

  public DataResponse<ReadResultResponse> readResult(UUID leagueGameId) {
    LeagueGame lg = findById(leagueGameId);
    ReadResultResponse response = ReadResultResponse.from(lg, lg.getLeague(), lg.getGame());
    return new DataResponse<>(200, "조회 완료", response);
  }

  public DataResponse<GenericPagedModel<ReadLeagueGameListResponse>> readResultList(
      UUID league, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<LeagueGame> lgs = resultJpaRepository.findAllByLeagueAndIsDeletedFalse(league, pageable);
    Page<ReadLeagueGameListResponse> response = lgs.map(ReadLeagueGameListResponse::from);
    return new DataResponse<>(200, "조회 완료", GenericPagedModel.of(response));
  }

  public DataResponse<Void> updateResult(UUID leagueGameId, UpdateLeagueGameRequest request) {
    validateUserHasAdminOrMasterRole();
    LeagueGame lg = findById(leagueGameId);
    lg.update(request.homeScore(), request.awayScore());
    resultJpaRepository.save(lg);
    return new DataResponse<>(200, "수정 완료", null);
  }

  public DataResponse<Void> deleteResut(UUID leagueGameId) {
    validateUserHasAdminOrMasterRole();
    LeagueGame lg = findById(leagueGameId);
    lg.delete(RequestUtils.getCurrentUserId());
    resultJpaRepository.save(lg);
    return new DataResponse<>(200, "삭제 완료", null);
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
