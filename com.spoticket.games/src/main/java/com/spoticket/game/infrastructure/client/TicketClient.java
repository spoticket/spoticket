package com.spoticket.game.infrastructure.client;

import com.spoticket.game.dto.response.TicketResponse;
import com.spoticket.game.dto.response.TicketStatus;
import com.spoticket.game.global.util.ResponseUtils.DataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ticket")
public interface TicketClient {

  @GetMapping("/api/v1/tickets")
  DataResponse<Page<TicketResponse>> getTickets(
      @RequestParam(required = false) TicketStatus status,
      @RequestParam(defaultValue = "10") int size
  );

}
