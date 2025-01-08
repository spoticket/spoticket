package com.spoticket.game.infrastructure.client;

import com.spoticket.game.dto.response.ApiResponse;
import com.spoticket.game.dto.response.TicketResponse;
import com.spoticket.game.dto.response.TicketStatus;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ticket")
public interface TicketClient {

  @GetMapping("/api/v1/tickets")
  ApiResponse<Page<TicketResponse>> getTickets(
      @RequestParam(required = false) UUID userId,
      @RequestParam(required = false) UUID gameId,
      @RequestParam(required = false) UUID seatId,
      @RequestParam(required = false) String seatName,
      @RequestParam(required = false) TicketStatus status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  );

}
