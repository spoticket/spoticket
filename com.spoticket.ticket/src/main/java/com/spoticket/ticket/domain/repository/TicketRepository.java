package com.spoticket.ticket.domain.repository;

import com.spoticket.ticket.domain.entity.Ticket;
import com.spoticket.ticket.domain.entity.TicketStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, UUID>,
    TicketRepositoryCustom {

  boolean existsBySeatNameAndStatusIn(String seatName, List<TicketStatus> statuses);

  List<Ticket> findAllBySeatId(UUID seatId);

}
