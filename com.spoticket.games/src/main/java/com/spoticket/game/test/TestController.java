package com.spoticket.game.test;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.repository.GameRepository;
import com.spoticket.game.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.spoticket.game.global.util.ResponseUtils.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final GameRepository gameRepository;

    @GetMapping("/api/v1/games/test")
    public DataResponse<Game> testGame() {
        String uuid = "83f697f8-0d32-4cd6-97a1-5a8c970e1ee7";
        Game game = gameRepository.findById(UUID.fromString(uuid)).orElseThrow();
        return ok(game);
    }

    @GetMapping("/api/v1/games/exception")
    public DataResponse<Game> testException() throws CustomException {
        throw new CustomException(BAD_REQUEST.value(), BAD_REQUEST.getReasonPhrase());
    }

}
