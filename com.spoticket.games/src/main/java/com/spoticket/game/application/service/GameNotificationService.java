package com.spoticket.game.application.service;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spoticket.game.dto.response.GameResponse;
import com.spoticket.game.dto.response.SuccessResponse;
import com.spoticket.game.dto.response.TicketResponse;
import com.spoticket.game.dto.response.TicketStatus;
import com.spoticket.game.dto.response.UserResponseDto;
import com.spoticket.game.infrastructure.client.TicketClient;
import com.spoticket.game.infrastructure.client.UserClient;
import com.spoticket.game.infrastructure.slack.SlackClient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final TicketClient ticketClient;
  private final UserClient userClient;

  @Scheduled(cron = "*/5 * * * * *")
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
    return ticketClient
        .getTickets(TicketStatus.BOOKED, Integer.MAX_VALUE)
        .getData()
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
