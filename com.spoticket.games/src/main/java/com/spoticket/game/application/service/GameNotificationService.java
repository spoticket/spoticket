package com.spoticket.game.application.service;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;

import com.spoticket.game.dto.response.GameResponse;
import com.spoticket.game.dto.response.TicketResponse;
import com.spoticket.game.infrastructure.slack.SlackClient;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameNotificationService {

  public static final String ONE_HOUR_NOTIFICATION = "경기 시작 1시간 전! 잊지 않으셨죠?";
  public static final String ONE_DAY_NOTIFICATION = "경기 시작 하루 전! 잊지 않으셨죠?";
  public static final String MOCK_SLACK_ID = "U07QT82GYBB";
  private final GameQueryService gameQueryService;
  private final SlackClient slackClient;

  @Scheduled(cron = "0 * * * * *")
  public void sendNotificationIfTimeMatches() {
//    List<TicketResponse> ticketResponses = ticketClient.getAllTickets();
    List<TicketResponse> ticketResponses = null;
    ticketResponses.forEach(ticketResponse -> {
      GameResponse gameResponse = gameQueryService.getGame(ticketResponse.gameId());
      sendNotificationIfTimeMatches(ticketResponse, gameResponse, now().plusHours(1),
          ONE_HOUR_NOTIFICATION);
      sendNotificationIfTimeMatches(ticketResponse, gameResponse, now().plusDays(1),
          ONE_DAY_NOTIFICATION);
    });
  }

  private void sendNotificationIfTimeMatches(TicketResponse ticket, GameResponse gameResponse,
      LocalDateTime targetTime,
      String message) {
    if (truncateToMinutes(targetTime).isEqual(truncateToMinutes(gameResponse.getStartTime()))) {
//      String slackId = userClient.findById(ticket.userId()).orElseThrow().getSlackId();
      String slackId = MOCK_SLACK_ID;
      slackClient.sendMessage(slackId, message);
      log.info("GameNotificationService.sendNotification");
    }
  }

  private LocalDateTime truncateToMinutes(LocalDateTime dateTime) {
    return dateTime.truncatedTo(MINUTES);
  }

}
