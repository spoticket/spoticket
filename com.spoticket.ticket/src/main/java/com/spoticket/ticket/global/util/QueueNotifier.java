package com.spoticket.ticket.global.util;

import com.spoticket.ticket.application.service.QueueService;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueNotifier {

  private static final Logger logger = LoggerFactory.getLogger(QueueNotifier.class);
  private final QueueService queueService;

  @Scheduled(fixedRate = 2000) // 2초마다 실행
  public void notifyQueueUpdates() {
    UUID gameId = UUID.fromString("532369d1-45c3-417d-86d0-cdd0a3bd1750"); // 예제용 Game ID
    int batchSize = 180; // 한번에 제거할 사용자 수

    // 대기열 가져오기
    Set<String> queue = queueService.getQueue(gameId);
    if (queue == null || queue.isEmpty()) {
      return;
    }

    // 상위 N명 가져오기
    Set<String> topUsers = queueService.getTopNFromQueue(gameId, batchSize);
    // 상위 N명 제거
    queueService.removeUsersFromQueue(gameId, topUsers);
    // 현재 대기열 크기 로그 출력
    logger.info("큐에서 사용자들 제거 후 대기열 크기: {}", queueService.getQueueSize(gameId));
  }
}
