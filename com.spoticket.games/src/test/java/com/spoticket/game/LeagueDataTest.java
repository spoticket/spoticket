package com.spoticket.game;

import com.spoticket.game.application.service.LeagueService;
import com.spoticket.game.application.service.LeagueUpdateService;
import com.spoticket.game.global.util.dummy.TestDataGenerator;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LeagueDataTest {

  @Autowired
  private TestDataGenerator testDataGenerator;

  @Autowired
  private LeagueUpdateService leagueUpdateService;

  @Autowired
  private LeagueService leagueService;


  @Test
  void generateDummyData() {
    testDataGenerator.generateData();
  }

  @Test
  void testProcessLeagueUpdates() {
    long startTime = System.currentTimeMillis();
    Runtime runtime = Runtime.getRuntime();
    long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

    System.out.println("League Update 시작: " + LocalDateTime.now());
    System.out.println("메모리 사용량(시작 전): " + usedMemoryBefore / 1024 / 1024 + " MB");

    leagueService.updateRankings();

    long endTime = System.currentTimeMillis();
    long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

    System.out.println("League Update 완료: " + LocalDateTime.now());
    System.out.println("총 실행 시간: " + (endTime - startTime) + " ms");
    System.out.println("메모리 사용량(종료 후): " + usedMemoryAfter / 1024 / 1024 + " MB");
  }
}
