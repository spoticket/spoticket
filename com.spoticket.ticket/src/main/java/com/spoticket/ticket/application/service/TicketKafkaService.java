package com.spoticket.ticket.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spoticket.ticket.application.dtos.request.UpdateTicketStatusRequest;
import com.spoticket.ticket.domain.entity.TicketStatus;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketKafkaService {

  private final TicketService ticketService;
  private final ObjectMapper objectMapper;

  @KafkaListener(
      topics = "${ticket.used.topic}",
      groupId = "${spring.kafka.consumer.group-id}"
  )
  @Transactional
  public void consumeTicketIds(String message) {
    try {
      List<UUID> ticketIds = parseTicketIds(message);
      for (UUID ticketId : ticketIds) {
        updateTicketStatus(ticketId);
      }
    } catch (Exception e) {
      log.error("메시지 처리 중 오류 발생 - message: {}", message, e);
    }
  }

  private List<UUID> parseTicketIds(String message) throws Exception {
    try {
      return objectMapper.readValue(message, new TypeReference<List<UUID>>() {
      });
    } catch (Exception e) {
      log.error("메시지 파싱 중 오류 발생 - message: {}", message, e);
      throw e;
    }
  }

  private void updateTicketStatus(UUID ticketId) {
    try {
      ticketService.updateTicketStatus(ticketId,
          new UpdateTicketStatusRequest(TicketStatus.BOOKED));
      log.info("티켓 상태 업데이트 성공 - ticketId: {}", ticketId);
    } catch (Exception e) {
      log.error("티켓 상태 업데이트 중 오류 발생 - ticketId: {}", ticketId, e);
    }
  }

}

