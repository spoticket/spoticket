package com.spoticket.ticket.presentation;

import com.spoticket.ticket.application.dtos.request.CreateTicketRequest;
import com.spoticket.ticket.application.dtos.request.UpdateTicketStatusRequest;
import com.spoticket.ticket.application.dtos.response.TicketInfoResponse;
import com.spoticket.ticket.application.dtos.response.TicketResponse;
import com.spoticket.ticket.application.service.TicketService;
import com.spoticket.ticket.global.exception.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping("/{ticketId}")
  public ApiResponse<TicketInfoResponse> getTicketById(@PathVariable UUID ticketId) {
    TicketInfoResponse ticketInfoResponse = ticketService.getTicketById(ticketId);

    return ApiResponse.success(ticketInfoResponse, "조회 성공");
  }

  @PatchMapping("/{ticketId}")
  public ApiResponse<TicketResponse> updateTicketStatus(@PathVariable UUID ticketId,
      @RequestBody UpdateTicketStatusRequest request) {
    TicketResponse ticketResponse = ticketService.updateTicketStatus(ticketId, request);

    return ApiResponse.success(ticketResponse, "티켓 상태 변경 완료");
  }

  @DeleteMapping("/{ticketId}")
  public ApiResponse<TicketResponse> deleteTicket(@PathVariable UUID ticketId) {
    TicketResponse ticketResponse = ticketService.deleteTicket(ticketId);

    return ApiResponse.success(ticketResponse, "티켓이 성공적으로 삭제되었습니다.");
  }

}
