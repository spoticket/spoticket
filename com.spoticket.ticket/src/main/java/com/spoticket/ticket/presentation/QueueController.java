package com.spoticket.ticket.presentation;

import com.spoticket.ticket.application.dtos.request.UserWaitRequest;
import com.spoticket.ticket.application.service.QueueService;
import com.spoticket.ticket.global.util.UserContextUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wait")
public class QueueController {

  private final QueueService queueService;
  private final UserContextUtil userContextUtil;

  // 현재 사용자 대기열 진입
  @PostMapping("/{gameId}")
  public String addToQueue(@PathVariable UUID gameId) {
    queueService.addToQueue(gameId, userContextUtil.getUserId());
    return "User added to queue.";
  }

  // 현재 사용자 대기열에서 삭제
  @DeleteMapping("/{gameId}")
  public String removeFromQueue(@PathVariable UUID gameId) {
    queueService.removeFromQueue(gameId, userContextUtil.getUserId());
    return "User removed from queue.";
  }

  // 현재 사용자 순위
  @GetMapping("/{gameId}/position")
  public long getPosition(@PathVariable UUID gameId) {
    return queueService.getPositionInQueue(gameId, userContextUtil.getUserId());
  }

  // 현재 대기열에 있는 사람들 수
  @GetMapping("/{gameId}/size")
  public long getQueueSize(@PathVariable UUID gameId) {
    return queueService.getQueueSize(gameId);
  }

  /*
    테스트용 API
   */
  // 대기열에 사용자 추가
  @PostMapping("/test/{gameId}")
  public String addRandomUserToQueue(@PathVariable UUID gameId,
      @RequestBody UserWaitRequest request) {
    queueService.addToQueue(gameId, request.userId());
    return "User added to queue.";
  }

  // 특정 사용자 대기 순위 조회
  @GetMapping("/{gameId}/position/{userId}")
  public long getPositionForUser(@PathVariable UUID gameId, @PathVariable UUID userId) {
    return queueService.getPositionInQueue(gameId, userId);
  }

}
