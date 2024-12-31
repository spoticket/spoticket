package com.spoticket.game;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class GameApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(LocalDateTime.now());
    }

}
