package com.spoticket.ticket.application.service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

  private static final Logger logger = LoggerFactory.getLogger(QueueService.class);
  private final RedisTemplate<String, String> waitTemplate;
  private final ZSetOperations<String, String> zSetOps;

  public QueueService(
      RedisTemplate<String, String> waitTemplate
  ) {
    this.waitTemplate = waitTemplate;
    this.zSetOps = waitTemplate.opsForZSet();
  }

  private static final String QUEUE_KEY_PREFIX = "wait:";

  // 대기열에 사용자 추가
  public void addToQueue(UUID gameId, UUID userId) {
    String key = QUEUE_KEY_PREFIX + gameId;
    double score = Instant.now().toEpochMilli();
    zSetOps.add(key, String.valueOf(userId), score);
  }

  // 대기열에서 사용자 제거
  public void removeFromQueue(UUID gameId, UUID userId) {
    String key = QUEUE_KEY_PREFIX + gameId;
    zSetOps.remove(key, userId);
  }

  // 대기열 조회
  public Set<String> getQueue(UUID gameId) {
    String key = QUEUE_KEY_PREFIX + gameId;
    return zSetOps.range(key, 0, -1);
  }

  // 상위 N명 가져오기
  public Set<String> getTopNFromQueue(UUID gameId, int n) {
    String key = QUEUE_KEY_PREFIX + gameId;
    return zSetOps.range(key, 0, n - 1); // 상위 N명 가져오기
  }

  // 여러 사용자 제거
  public void removeUsersFromQueue(UUID gameId, Set<String> userIds) {
    String key = QUEUE_KEY_PREFIX + gameId;
    // 로그 추가
    logger.info("대기열에서 사용자 제거-경기ID: {}, 사용자ID: {}", gameId, userIds);

    zSetOps.remove(key, userIds.toArray());

    // 결과 확인 로그 추가
    logger.info("성공적으로 대기열에서 제거: {}", gameId);
  }

  // 앞에 몇명 있는지 조회
  public long getPositionInQueue(UUID gameId, UUID userId) {
    String key = QUEUE_KEY_PREFIX + gameId;
    Long rank = zSetOps.rank(key, userId);
    return rank != null ? rank + 1 : -1;
  }

  // 대기열 크기 확인
  public long getQueueSize(UUID gameId) {
    String key = QUEUE_KEY_PREFIX + gameId;
    return zSetOps.size(key);
  }

}
