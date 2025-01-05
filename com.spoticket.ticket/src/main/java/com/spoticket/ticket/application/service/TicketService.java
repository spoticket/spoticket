package com.spoticket.ticket.application.service;

import com.spoticket.ticket.application.dtos.request.CreateTicketRequest;
import com.spoticket.ticket.application.dtos.response.TicketResponse;
import com.spoticket.ticket.domain.entity.Ticket;
import com.spoticket.ticket.domain.entity.TicketStatus;
import com.spoticket.ticket.domain.repository.TicketRepository;
import com.spoticket.ticket.global.exception.BusinessException;
import com.spoticket.ticket.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;

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
}
