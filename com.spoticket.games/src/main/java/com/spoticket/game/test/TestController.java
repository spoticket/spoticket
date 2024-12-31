package com.spoticket.game.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/api/v1/games/test")
    public String testGame() {
        return "TestController.testGame";
    }

}
