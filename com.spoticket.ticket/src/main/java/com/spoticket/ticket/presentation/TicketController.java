package com.spoticket.ticket.presentation;

import com.spoticket.ticket.application.dtos.request.CreateTicketRequest;
import com.spoticket.ticket.application.dtos.request.TicketSearchCriteria;
import com.spoticket.ticket.application.dtos.request.UpdateTicketStatusRequest;
import com.spoticket.ticket.application.dtos.response.TicketInfoResponse;
import com.spoticket.ticket.application.dtos.response.TicketResponse;
import com.spoticket.ticket.application.service.TicketService;
import com.spoticket.ticket.domain.entity.TicketStatus;
import com.spoticket.ticket.global.exception.BusinessException;
import com.spoticket.ticket.global.exception.ErrorCode;
import com.spoticket.ticket.global.util.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    System.out.println("Requested ticketId = " + ticketId);  // 로그 추가

    TicketInfoResponse ticketInfoResponse = ticketService.getTicketById(ticketId);

    System.out.println("ticketInfoResponse = " + ticketInfoResponse);  // 로그 추가

    return ApiResponse.success(ticketInfoResponse, "조회 성공");
  }

  @GetMapping
  public ApiResponse<Page<TicketResponse>> getTickets(
      @RequestParam(required = false) UUID userId,
      @RequestParam(required = false) UUID gameId,
      @RequestParam(required = false) UUID seatId,
      @RequestParam(required = false) String seatName,
      @RequestParam(required = false) TicketStatus status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    TicketSearchCriteria criteria = new TicketSearchCriteria(userId, gameId, seatId, seatName,
        status, page, size);
    Page<TicketResponse> tickets = ticketService.getTickets(criteria);

    if (tickets.isEmpty()) {
      throw new BusinessException(ErrorCode.TICKET_NOT_FOUND);
    }

    return ApiResponse.success(tickets, "조회 성공");
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
