package com.spoticket.ticket.application.service;

import com.spoticket.ticket.application.dtos.request.CreateTicketRequest;
import com.spoticket.ticket.application.dtos.request.TicketSearchCriteria;
import com.spoticket.ticket.application.dtos.request.UpdateTicketStatusRequest;
import com.spoticket.ticket.application.dtos.response.GameResponse;
import com.spoticket.ticket.application.dtos.response.SeatReadResponse;
import com.spoticket.ticket.application.dtos.response.StadiumInfoResponse;
import com.spoticket.ticket.application.dtos.response.StadiumReadResponse;
import com.spoticket.ticket.application.dtos.response.TicketInfoResponse;
import com.spoticket.ticket.application.dtos.response.TicketResponse;
import com.spoticket.ticket.application.dtos.response.UserResponseDto;
import com.spoticket.ticket.domain.entity.Ticket;
import com.spoticket.ticket.domain.entity.TicketStatus;
import com.spoticket.ticket.domain.repository.TicketRepository;
import com.spoticket.ticket.global.config.redisson.DistributedLock;
import com.spoticket.ticket.global.exception.BusinessException;
import com.spoticket.ticket.global.exception.ErrorCode;
import com.spoticket.ticket.global.util.ApiResponse;
import com.spoticket.ticket.global.util.UserContextUtil;
import com.spoticket.ticket.infrastructure.GameServiceClient;
import com.spoticket.ticket.infrastructure.StadiumServiceClient;
import com.spoticket.ticket.infrastructure.UserServiceClient;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {


  private final TicketRepository ticketRepository;
  private final UserContextUtil userContextUtil;
  private final GameServiceClient gameServiceClient;
  private final StadiumServiceClient stadiumServiceClient;
  private final UserServiceClient userServiceClient;

  private static final String CACHE_KEY_SEAT_TICKETS = "tickets::seatId:";

  private final RedisTemplate<String, List<TicketResponse>> seatListTemplate;


  @DistributedLock(key = "#request.seatName")
  public TicketResponse createTicket(CreateTicketRequest request) {
    // 좌석이 이미 예약되었는지 확인
    boolean isSeatAlreadyReserved = ticketRepository.existsBySeatNameAndStatus(request.seatName(),
        TicketStatus.PICKED);
    if (isSeatAlreadyReserved) {
      throw new BusinessException(ErrorCode.SEAT_ALREADY_RESERVED); // 좌석 예약 실패
    }

    Ticket ticket = Ticket.create(
        userContextUtil.getUserId(),
        request.gameId(),
        request.stadiumId(),
        request.seatId(),
        request.seatName()
    );

    Ticket savedTicket = ticketRepository.save(ticket);

    updateSeatTicketsCache(request.seatId());

    return TicketResponse.from(savedTicket);
  }

  @Transactional(readOnly = true)
  public TicketInfoResponse getTicketById(UUID ticketId) {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new BusinessException(ErrorCode.TICKET_NOT_FOUND));

    // 게임 정보 가져오기
    ApiResponse<GameResponse> gameResponse = gameServiceClient.getGame(ticket.getGameId());
    GameResponse gameData = gameResponse.getData();

    // 경기장 정보 가져오기
    ApiResponse<StadiumReadResponse> stadiumResponse = stadiumServiceClient.getStadiumInfo(
        ticket.getStadiumId());
    StadiumInfoResponse stadiumData = stadiumResponse.getData().stadium();

    // 좌석 정보 가져오기
    ApiResponse<SeatReadResponse> seatResponse = stadiumServiceClient.getSeat(
        ticket.getSeatId());
    SeatReadResponse seatData = seatResponse.getData();

    // 사용자 정보 가져오기
    ApiResponse<UserResponseDto> userResponse = userServiceClient.selectUserById(
        ticket.getUserId());
    UserResponseDto userData = userResponse.getData();

    return TicketInfoResponse.from(ticket, userData.name(), gameData.title(), stadiumData.name(),
        seatData.section(), seatData.price());
  }

  @Transactional(readOnly = true)
  public Page<TicketResponse> getTickets(TicketSearchCriteria criteria) {
    Page<Ticket> tickets = ticketRepository.findAllByCriteria(criteria);
    return tickets.map(TicketResponse::from);
  }

  @Cacheable(value = "tickets", key = "'seatId:' + #seatId")
  public List<TicketResponse> getTicketsBySeatId(UUID seatId) {
    List<Ticket> tickets = ticketRepository.findAllBySeatId(seatId);
    return tickets.stream()
        .map(TicketResponse::from)
        .collect(Collectors.toList());
  }

  @Transactional
  //@CacheEvict(value = "tickets", key = "'seatId:' + #seatId")
  public TicketResponse updateTicketStatus(UUID ticketId, UpdateTicketStatusRequest request) {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new BusinessException(ErrorCode.TICKET_NOT_FOUND));

    ticket.update(request.status());

    updateSeatTicketsCache(ticket.getSeatId());

    return TicketResponse.from(ticket);
  }

  @Transactional
  //@CacheEvict(value = "tickets", key = "'seatId:' + #ticket.getSeatId")
  public TicketResponse deleteTicket(UUID ticketId) {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new BusinessException(ErrorCode.TICKET_NOT_FOUND));
    ticket.update(TicketStatus.DELETED);

    updateSeatTicketsCache(ticket.getSeatId());

    return TicketResponse.from(ticket);
  }

  private void updateSeatTicketsCache(UUID seatId) {
    List<TicketResponse> tickets = ticketRepository.findAllBySeatId(seatId).stream()
        .map(TicketResponse::from)
        .collect(Collectors.toList());
    seatListTemplate.opsForValue().set(CACHE_KEY_SEAT_TICKETS + seatId, tickets,
        Duration.ofHours(1));
  }


}
