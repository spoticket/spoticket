package com.spoticket.ticket.domain.repository;

import com.spoticket.ticket.domain.entity.Ticket;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

}
