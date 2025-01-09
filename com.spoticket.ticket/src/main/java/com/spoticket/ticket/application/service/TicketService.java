package com.spoticket.ticket.application.service;

import com.spoticket.ticket.application.dtos.request.CreateTicketRequest;
import com.spoticket.ticket.application.dtos.request.TicketSearchCriteria;
import com.spoticket.ticket.application.dtos.request.UpdateTicketStatusRequest;
import com.spoticket.ticket.application.dtos.response.GameResponse;
import com.spoticket.ticket.application.dtos.response.TicketInfoResponse;
import com.spoticket.ticket.application.dtos.response.TicketResponse;
import com.spoticket.ticket.domain.entity.Ticket;
import com.spoticket.ticket.domain.entity.TicketStatus;
import com.spoticket.ticket.domain.repository.TicketRepository;
import com.spoticket.ticket.global.exception.BusinessException;
import com.spoticket.ticket.global.exception.ErrorCode;
import com.spoticket.ticket.global.util.ApiResponse;
import com.spoticket.ticket.infrastructure.GameServiceClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {


  private final TicketRepository ticketRepository;
  private final GameServiceClient gameServiceClient;

  @Transactional
  public TicketResponse createTicket(CreateTicketRequest request) {
    // 좌석이 이미 예약되었는지 확인
    boolean isSeatAlreadyReserved = ticketRepository.existsBySeatNameAndStatus(request.seatName(),
        TicketStatus.PICKED);
    if (isSeatAlreadyReserved) {
      throw new BusinessException(ErrorCode.SEAT_ALREADY_RESERVED); // 좌석 예약 실패
    }

    Ticket ticket = Ticket.create(
        request.userId(),
        request.gameId(),
        request.seatId(),
        request.seatName()
    );

    return TicketResponse.from(ticketRepository.save(ticket));
  }

  public TicketInfoResponse getTicketById(UUID ticketId) {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new BusinessException(ErrorCode.TICKET_NOT_FOUND));

    // 게임 정보 가져오기
    ApiResponse<GameResponse> gameResponse = gameServiceClient.getGame(ticket.getGameId());
    GameResponse gameData = gameResponse.getData();

    return TicketInfoResponse.from(ticket, gameData.title());
  }

  public Page<TicketResponse> getTickets(TicketSearchCriteria criteria) {
    Page<Ticket> tickets = ticketRepository.findAllByCriteria(criteria);
    return tickets.map(TicketResponse::from);
  }

  @Transactional
  public TicketResponse updateTicketStatus(UUID ticketId, UpdateTicketStatusRequest request) {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new BusinessException(ErrorCode.TICKET_NOT_FOUND));

    ticket.update(request.status());

    return TicketResponse.from(ticket);
  }

  @Transactional
  public TicketResponse deleteTicket(UUID ticketId) {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new BusinessException(ErrorCode.TICKET_NOT_FOUND));

    ticket.update(TicketStatus.DELETED);

    return TicketResponse.from(ticket);
  }


}
