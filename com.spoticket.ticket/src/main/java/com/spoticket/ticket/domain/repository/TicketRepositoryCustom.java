package com.spoticket.ticket.domain.repository;

import com.spoticket.ticket.application.dtos.request.TicketSearchCriteria;
import com.spoticket.ticket.domain.entity.Ticket;
import org.springframework.data.domain.Page;

public interface TicketRepositoryCustom {

  Page<Ticket> findAllByCriteria(TicketSearchCriteria criteria);
}
