package com.spoticket.user.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

  private final TeamStadiumClient teamStadiumClient;
  private final GameClient gameClient;
  private final PaymentClient paymentClient;
  private final TicketClient ticketClient;

  @GetMapping("/api/v1/users/team-stadium/test")
  public String teamStadiumTest() {
    return teamStadiumClient.test();
  }

  @GetMapping("/api/v1/users/game/test")
  public String gameTest() {
    return gameClient.test();
  }

  @GetMapping("/api/v1/users/payment/test")
  public String paymentTest() {
    return paymentClient.test();
  }

  @GetMapping("/api/v1/users/ticket/test")
  public String ticketTest() {
    return ticketClient.test();
  }

}
