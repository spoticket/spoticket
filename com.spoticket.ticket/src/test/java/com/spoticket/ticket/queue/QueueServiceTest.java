package com.spoticket.ticket.queue;

import static org.assertj.core.api.Assertions.assertThat;

import com.spoticket.ticket.application.service.QueueService;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class QueueServiceTest {

  @Autowired
  private QueueService queueService;

  @Autowired
  private RedisTemplate<String, String> waitTemplate;

  private static final UUID GAME_ID = UUID.randomUUID(); // 고정된 게임 ID

  @BeforeEach
  void setUp() {
    // Redis 초기화
    waitTemplate.delete("wait:" + GAME_ID);
  }

  @Test
  void addToQueue_shouldAddUserToQueue() {
    UUID userId = UUID.randomUUID();

    queueService.addToQueue(GAME_ID, userId);

    Set<String> queue = queueService.getQueue(GAME_ID);
    assertThat(queue).contains(userId.toString());
  }

  @Test
  void getQueue_shouldReturnAllUsersInQueue() {
    UUID user1 = UUID.randomUUID();
    UUID user2 = UUID.randomUUID();

    queueService.addToQueue(GAME_ID, user1);
    queueService.addToQueue(GAME_ID, user2);

    Set<String> queue = queueService.getQueue(GAME_ID);
    assertThat(queue).containsExactly(user1.toString(), user2.toString());
  }

  @Test
  void getPositionInQueue_shouldReturnCorrectPosition() {
    UUID user1 = UUID.randomUUID();
    UUID user2 = UUID.randomUUID();

    queueService.addToQueue(GAME_ID, user1);
    queueService.addToQueue(GAME_ID, user2);

    long position1 = queueService.getPositionInQueue(GAME_ID, user1);
    long position2 = queueService.getPositionInQueue(GAME_ID, user2);

    assertThat(position1).isEqualTo(0);
    assertThat(position2).isEqualTo(1);
  }

  @Test
  void removeFromQueue_shouldRemoveUserFromQueue() {
    UUID userId = UUID.randomUUID();

    queueService.addToQueue(GAME_ID, userId);
    queueService.removeFromQueue(GAME_ID, userId);

    Set<String> queue = queueService.getQueue(GAME_ID);
    assertThat(queue).doesNotContain(userId.toString());
  }

  @Test
  void getQueueSize_shouldReturnCorrectSize() {
    UUID user1 = UUID.randomUUID();
    UUID user2 = UUID.randomUUID();

    queueService.addToQueue(GAME_ID, user1);
    queueService.addToQueue(GAME_ID, user2);

    long size = queueService.getQueueSize(GAME_ID);
    assertThat(size).isEqualTo(2);
  }
}
