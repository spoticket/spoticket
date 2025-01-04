package com.spoticket.ticket.presentation;

import com.spoticket.ticket.application.dtos.request.CreateTicketRequest;
import com.spoticket.ticket.application.dtos.response.TicketResponse;
import com.spoticket.ticket.application.service.TicketService;
import com.spoticket.ticket.global.exception.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {

  private final TicketService ticketService;

  @PostMapping
  public ApiResponse<TicketResponse> createTicket(@RequestBody @Valid CreateTicketRequest request) {
    TicketResponse ticketResponse = ticketService.createTicket(request);

    return ApiResponse.success(ticketResponse, "티켓 예매 성공");
  }

}
