package com.spoticket.game.application.service;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spoticket.game.application.dto.response.GameResponse;
import com.spoticket.game.application.dto.response.SuccessResponse;
import com.spoticket.game.application.dto.response.TicketResponse;
import com.spoticket.game.application.dto.response.TicketStatus;
import com.spoticket.game.application.dto.response.UserResponseDto;
import com.spoticket.game.common.util.ApiResponse;
import com.spoticket.game.infrastructure.client.TicketServiceClient;
import com.spoticket.game.infrastructure.client.UserServiceClient;
import com.spoticket.game.infrastructure.slack.SlackClient;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameNotificationService {

  public static final String ONE_HOUR_NOTIFICATION = "경기 시작 1시간 전! 잊지 않으셨죠?";
  public static final String ONE_DAY_NOTIFICATION = "경기 시작 하루 전! 잊지 않으셨죠?";

  private final GameQueryService gameQueryService;
  private final SlackClient slackClient;
  private final TicketServiceClient ticketServiceClient;
  private final UserServiceClient userClient;

  @Scheduled(cron = "0 * * * * *")
  public void sendNotificationIfTimeMatches() {
    List<TicketResponse> ticketResponses = getAllBookedTickets();
    for (TicketResponse ticketResponse : ticketResponses) {
      GameResponse gameResponse = gameQueryService.getGame(ticketResponse.gameId());
      sendNotificationIfTimeMatches(ticketResponse, gameResponse, now().plusHours(1),
          ONE_HOUR_NOTIFICATION);
      sendNotificationIfTimeMatches(ticketResponse, gameResponse, now().plusDays(1),
          ONE_DAY_NOTIFICATION);
    }
  }

  private void sendNotificationIfTimeMatches(
      TicketResponse ticket, GameResponse gameResponse,
      LocalDateTime targetTime, String message
  ) {
    if (truncateToMinutes(targetTime).isEqual(truncateToMinutes(gameResponse.getStartTime()))) {
      UserResponseDto userResponseDto = getUserByUserId(ticket.userId());
      slackClient.sendMessage(userResponseDto.slackId(), message);
    }
  }

  private LocalDateTime truncateToMinutes(LocalDateTime dateTime) {
    return dateTime.truncatedTo(MINUTES);
  }

  private List<TicketResponse> getAllBookedTickets() {
    ApiResponse<Page<TicketResponse>> response = ticketServiceClient
        .getTickets(TicketStatus.BOOKED, Integer.MAX_VALUE);

    if (response.code() == NOT_FOUND.value()) {
      return Collections.emptyList();
    }

    return response
        .data()
        .stream()
        .toList();
  }

  private UserResponseDto getUserByUserId(UUID userId) {
    ObjectMapper objectMapper = new ObjectMapper();
    ResponseEntity<?> responseEntity = userClient.selectUserById(userId);
    SuccessResponse<UserResponseDto> response = objectMapper.convertValue(
        responseEntity.getBody(),
        objectMapper.getTypeFactory()
            .constructParametricType(SuccessResponse.class, UserResponseDto.class)
    );
    return response.data();
  }

}
