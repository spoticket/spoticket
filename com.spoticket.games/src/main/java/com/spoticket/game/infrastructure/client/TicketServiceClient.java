package com.spoticket.game.infrastructure.client;

import com.spoticket.game.application.dto.response.TicketResponse;
import com.spoticket.game.application.dto.response.TicketStatus;
import com.spoticket.game.common.util.ApiResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ticket")
public interface TicketServiceClient {

  @GetMapping("/api/v1/tickets")
  ApiResponse<Page<TicketResponse>> getTickets(
      @RequestParam(required = false) TicketStatus status,
      @RequestParam(defaultValue = "10") int size
  );

  @GetMapping("/api/v1/tickets")
  ApiResponse<Page<TicketResponse>> getTicketsByGameId(
      @RequestParam(required = false) UUID gameId,
      @RequestParam(required = false) TicketStatus status,
      @RequestParam(defaultValue = "10") int size
  );

}
