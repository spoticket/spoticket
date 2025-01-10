package com.spoticket.ticket.application.dtos.request;

import com.spoticket.ticket.domain.entity.TicketStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketSearchCriteria {

  private final UUID userId;
  private final UUID gameId;
  private final UUID seatId;
  private final String seatName;
  private final TicketStatus status;
  private final int page;
  private final int size;
}
