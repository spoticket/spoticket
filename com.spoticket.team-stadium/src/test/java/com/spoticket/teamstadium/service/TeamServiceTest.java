package com.spoticket.teamstadium.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spoticket.teamstadium.application.dto.request.TeamCreateRequest;
import com.spoticket.teamstadium.application.dto.request.TeamUpdateRequest;
import com.spoticket.teamstadium.application.dto.response.TeamListReadResponse;
import com.spoticket.teamstadium.application.dto.response.TeamReadResponse;
import com.spoticket.teamstadium.application.service.TeamService;
import com.spoticket.teamstadium.domain.model.FavTeam;
import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
import com.spoticket.teamstadium.domain.repository.FavTeamRepository;
import com.spoticket.teamstadium.domain.repository.TeamRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.exception.NotFoundException;
import com.spoticket.teamstadium.factory.TeamTestFactory;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import com.spoticket.teamstadium.global.dto.PaginatedResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

  @Mock
  private TeamRepository teamRepository;

  @Mock
  private FavTeamRepository favTeamRepository;

  @InjectMocks
  private TeamService teamService;

  // 팀 생성 테스트
  // 팀명 중복
  @Test
  void createTeam_WhenTeamNameIsDuplicate() {

    // Given
    String teamName = "Duplicate Team";
    TeamCreateRequest request = new TeamCreateRequest(
        teamName,
        TeamCategoryEnum.BASEBALL,
        "Description",
        "https://profile.com",
        "https://home.com",
        "https://sns.com"
    );

    when(teamRepository.findByNameAndIsDeletedFalse(teamName)).thenReturn(
        Optional.of(mock(Team.class)));

    // When & Then
    assertThatThrownBy(() -> teamService.createTeam(request))
        .isInstanceOf(BusinessException.class)
        .hasMessage(ErrorCode.DUPLICATE_TEAM_NAME.getMessage());

    verify(teamRepository, times(1)).findByNameAndIsDeletedFalse(teamName);

  }

  @Test
  void createTeam_WhenTeamIsCreatedSuccessfully() {

    // Given
    UUID teamId = UUID.randomUUID();
    Team mockTeam = TeamTestFactory.createWithId(
        teamId,
        "New Team",
        TeamCategoryEnum.BASEBALL,
        "test description",
        "https://profile.com",
        "https://home.com",
        "https://sns.com"
    );

    TeamCreateRequest request = new TeamCreateRequest(
        "New Team",
        TeamCategoryEnum.BASEBALL,
        "test description",
        "https://profile.com",
        "https://home.com",
        "https://sns.com"
    );

    when(teamRepository.findByNameAndIsDeletedFalse(request.name())).thenReturn(Optional.empty());
    when(teamRepository.save(any(Team.class))).thenReturn(mockTeam);

    ArgumentCaptor<Team> teamCaptor = ArgumentCaptor.forClass(Team.class);

    // When
    ApiResponse<Map<String, UUID>> response = teamService.createTeam(request);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.code()).isEqualTo(200);
    assertThat(response.msg()).isEqualTo("생성 완료");
    assertThat(response.data()).containsKey("teamId");
    assertThat(response.data()).containsEntry("teamId", teamId);

    verify(teamRepository).save(teamCaptor.capture());
    Team savedTeam = teamCaptor.getValue();

    assertThat(savedTeam.getName()).isEqualTo(request.name());
    assertThat(savedTeam.getCategory()).isEqualTo(request.category());
    assertThat(savedTeam.getDescription()).isEqualTo(request.description());
    assertThat(savedTeam.getProfile()).isEqualTo(request.profile());
    assertThat(savedTeam.getHomeLink()).isEqualTo(request.homeLink());
    assertThat(savedTeam.getSnsLink()).isEqualTo(request.snsLink());

    verify(teamRepository, times(1)).findByNameAndIsDeletedFalse(request.name());
    verify(teamRepository, times(1)).save(any(Team.class));
  }

  // 팀 단일 조회
  @Test
  void getTeamInfo_FeignClientIsExcluded() {
    // Given
    UUID teamId = UUID.randomUUID();
    Team mockTeam = Team.builder()
        .teamId(teamId)
        .name("Test Team")
        .description("test team")
        .build();

    long favCnt = 10;

    when(teamRepository.findByTeamIdAndIsDeletedFalse(teamId)).thenReturn(Optional.of(mockTeam));
    when(favTeamRepository.countByTeam_TeamId(teamId)).thenReturn(favCnt);

    // When
    ApiResponse<TeamReadResponse> response = teamService.getTeamInfo(teamId);

    // Then
    assertNotNull(response);
    assertEquals(200, response.code());

    TeamReadResponse teamReadResponse = response.data();
    assertEquals(teamId, teamReadResponse.team().teamId());
    assertEquals("Test Team", teamReadResponse.team().name());
    assertEquals(favCnt, teamReadResponse.team().favCnt());
    assertNull(teamReadResponse.games());
  }

  // 팀 목록 조회
  // 관심 팀 조회
  @Test
  void getTeams_WhenFavIsTrue() {
    // Given
    UUID userId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 10);

    Team team = Team.builder()
        .teamId(UUID.randomUUID())
        .name("Test Team")
        .category(TeamCategoryEnum.BASEBALL)
        .build();

    FavTeam favTeam = FavTeam.builder()
        .favId(UUID.randomUUID())
        .team(team)
        .userId(userId)
        .build();

    Page<FavTeam> favTeams = new PageImpl<>(List.of(favTeam), pageable, 1);
    Page<Team> teams = new PageImpl<>(List.of(team), pageable, 1);

    when(favTeamRepository.findAllByUserId(userId, pageable)).thenReturn(favTeams);
    when(teamRepository.findAllByTeamIdInAndIsDeletedFalse(anyList(), eq(pageable))).thenReturn(
        teams);

    // When
    PaginatedResponse<TeamListReadResponse> response = teamService.getTeams(null, true, userId, 0,
        10);

    // Then
    assertNotNull(response);
    assertEquals(1, response.totalElements());
    assertEquals("Test Team", response.content().get(0).name());

    verify(favTeamRepository, times(1)).findAllByUserId(userId, pageable);
    verify(teamRepository, times(1)).findAllByTeamIdInAndIsDeletedFalse(anyList(), eq(pageable));
  }

  // 카테고리별 조회
  @Test
  void getTeams_WhenCategoryIsGiven() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);

    Team team = Team.builder()
        .teamId(UUID.randomUUID())
        .name("Test Soccer Team")
        .category(TeamCategoryEnum.SOCCER)
        .build();

    Page<Team> teams = new PageImpl<>(List.of(team), pageable, 1);

    when(teamRepository.findAllByCategoryAndIsDeletedFalse(TeamCategoryEnum.SOCCER,
        pageable)).thenReturn(teams);

    // When
    PaginatedResponse<TeamListReadResponse> response = teamService.getTeams(TeamCategoryEnum.SOCCER,
        false, null, 0, 10);

    // Then
    assertNotNull(response);
    assertEquals(1, response.totalElements());
    assertEquals("Test Soccer Team", response.content().get(0).name());

    verify(teamRepository, times(1)).findAllByCategoryAndIsDeletedFalse(TeamCategoryEnum.SOCCER,
        pageable);
  }

  // 전체 팀 조회
  @Test
  void getTeams_WhenNoConditions() {
    // Given
    Pageable pageable = PageRequest.of(0, 10);

    Team team1 = Team.builder()
        .teamId(UUID.randomUUID())
        .name("Team A")
        .category(TeamCategoryEnum.BASEBALL)
        .build();

    Team team2 = Team.builder()
        .teamId(UUID.randomUUID())
        .name("Team B")
        .category(TeamCategoryEnum.SOCCER)
        .build();

    Page<Team> teams = new PageImpl<>(List.of(team1, team2), pageable, 2);

    when(teamRepository.findAllByIsDeletedFalse(pageable)).thenReturn(teams);

    // When
    PaginatedResponse<TeamListReadResponse> response = teamService.getTeams(null, false, null, 0,
        10);

    // Then
    assertNotNull(response);
    assertEquals(2, response.totalElements());
    assertEquals("Team A", response.content().get(0).name());
    assertEquals("Team B", response.content().get(1).name());

    verify(teamRepository, times(1)).findAllByIsDeletedFalse(pageable);
  }

  // 팀 수정
  @Test
  void updateTeam_success() {
    // Given
    UUID teamId = UUID.randomUUID();
    Team existingTeam = Team.builder()
        .teamId(teamId)
        .name("Old Team Name")
        .description("Old Description")
        .build();

    TeamUpdateRequest updateRequest = new TeamUpdateRequest(
        "New Team Name",
        "New Description",
        "New Profile",
        "New HomeLink",
        "New SNSLink"
    );

    when(teamRepository.findByTeamIdAndIsDeletedFalse(teamId)).thenReturn(
        Optional.of(existingTeam));
    when(teamRepository.findByNameAndIsDeletedFalse(updateRequest.name()))
        .thenReturn(Optional.empty());

    // When
    ApiResponse<TeamUpdateRequest> response = teamService.updateTeam(teamId, updateRequest);

    // Then
    assertEquals(200, response.code());
    assertEquals("수정 완료", response.msg());

    assertEquals("New Team Name", existingTeam.getName());
    assertEquals("New Description", existingTeam.getDescription());
    verify(teamRepository).save(existingTeam);
  }

  @Test
  void updateTeam_WhenTeamDoesNotExist() {
    // Given
    UUID teamId = UUID.randomUUID();
    TeamUpdateRequest updateRequest = new TeamUpdateRequest(
        "New Team Name",
        "New Description",
        "New Profile",
        "New HomeLink",
        "New SNSLink"
    );

    when(teamRepository.findByTeamIdAndIsDeletedFalse(teamId)).thenReturn(Optional.empty());

    // When, Then
    assertThatThrownBy(() -> teamService.updateTeam(teamId, updateRequest))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("해당하는 팀이 없습니다");

    verify(teamRepository, never()).save(any());
  }

  // 팀 삭제
  @Test
  void deleteTeam_success() {
    // Given
    UUID teamId = UUID.randomUUID();
    Team mockTeam = Team.builder()
        .teamId(UUID.randomUUID())
        .name("Test Soccer Team")
        .category(TeamCategoryEnum.SOCCER)
        .build();
    when(teamRepository.findByTeamIdAndIsDeletedFalse(teamId)).thenReturn(Optional.of(mockTeam));

    // When
    ApiResponse<Void> response = teamService.deleteTeam(teamId);

    // Then
    assertNotNull(response);
    assertEquals(200, response.code());
    assertEquals("삭제 완료", response.msg());
    assertTrue(mockTeam.isDeleted());
    verify(teamRepository, times(1)).save(mockTeam);
  }

  // 관심 팀
  @Test
  void FavTeam_Add() {
    // Given
    UUID teamId = UUID.fromString("fc3514a1-c4c9-4c07-ab62-f99719797712");
    UUID userId = UUID.fromString("6844ee91-b725-4606-b06a-df7c7a58e452");

    Team mockTeam = Team.builder()
        .teamId(teamId)
        .name("Test Team")
        .build();

    when(teamRepository.findByTeamIdAndIsDeletedFalse(teamId)).thenReturn(Optional.of(mockTeam));
    when(favTeamRepository.findByUserIdAndTeam_TeamId(userId, teamId)).thenReturn(Optional.empty());

    ArgumentCaptor<FavTeam> favTeam = ArgumentCaptor.forClass(FavTeam.class);

    // When
    ApiResponse<Void> response = teamService.favTeam(teamId);

    // Then
    assertNotNull(response);
    assertEquals(200, response.code());
    assertEquals("관심 팀에 추가되었습니다", response.msg());

    verify(favTeamRepository).save(favTeam.capture());
    FavTeam savedFavTeam = favTeam.getValue();

    assertEquals(userId, savedFavTeam.getUserId());
    assertEquals(mockTeam, savedFavTeam.getTeam());
  }

  @Test
  void FavTeam_Remove() {
    // Given
    UUID teamId = UUID.fromString("fc3514a1-c4c9-4c07-ab62-f99719797712");
    UUID userId = UUID.fromString("6844ee91-b725-4606-b06a-df7c7a58e452");

    Team mockTeam = Team.builder()
        .teamId(teamId)
        .name("Test Team")
        .build();

    FavTeam mockFavTeam = FavTeam.builder()
        .favId(UUID.randomUUID())
        .userId(userId)
        .team(mockTeam)
        .build();

    when(teamRepository.findByTeamIdAndIsDeletedFalse(teamId)).thenReturn(Optional.of(mockTeam));
    when(favTeamRepository.findByUserIdAndTeam_TeamId(userId, teamId)).thenReturn(
        Optional.of(mockFavTeam));

    // When
    ApiResponse<Void> response = teamService.favTeam(teamId);

    // Then
    assertNotNull(response);
    assertEquals(200, response.code());
    assertEquals("관심 팀에서 삭제되었습니다", response.msg());

    verify(favTeamRepository).delete(mockFavTeam);
  }

}
