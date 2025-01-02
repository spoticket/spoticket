package com.spoticket.teamstadium.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spoticket.teamstadium.application.dto.request.TeamCreateRequest;
import com.spoticket.teamstadium.application.service.TeamService;
import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
import com.spoticket.teamstadium.domain.repository.TeamRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.factory.TeamTestFactory;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

  @Mock
  private TeamRepository teamRepository;

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

    when(teamRepository.findByName(teamName)).thenReturn(Optional.of(mock(Team.class)));

    // When & Then
    assertThatThrownBy(() -> teamService.createTeam(request))
        .isInstanceOf(BusinessException.class)
        .hasMessage(ErrorCode.DUPLICATE_TEAM_NAME.getMessage());

    verify(teamRepository, times(1)).findByName(teamName);

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

    when(teamRepository.findByName(request.name())).thenReturn(Optional.empty());
    when(teamRepository.save(any(Team.class))).thenReturn(mockTeam);

    ArgumentCaptor<Team> teamCaptor = ArgumentCaptor.forClass(Team.class);

    // When
    ApiResponse<Map<String, UUID>> response = teamService.createTeam(request);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.status()).isEqualTo(200);
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

    verify(teamRepository, times(1)).findByName(request.name());
    verify(teamRepository, times(1)).save(any(Team.class));
  }
}
