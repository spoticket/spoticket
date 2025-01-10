package com.spoticket.ticket.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spoticket.ticket.application.dtos.request.TicketSearchCriteria;
import com.spoticket.ticket.domain.entity.QTicket;
import com.spoticket.ticket.domain.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TicketRepositoryImpl implements TicketRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Ticket> findAllByCriteria(TicketSearchCriteria criteria) {
    QTicket ticket = QTicket.ticket;

    BooleanBuilder builder = new BooleanBuilder();
    if (criteria.getUserId() != null) {
      builder.and(ticket.userId.eq(criteria.getUserId()));
    }
    if (criteria.getGameId() != null) {
      builder.and(ticket.gameId.eq(criteria.getGameId()));
    }
    if (criteria.getStatus() != null) {
      builder.and(ticket.status.eq(criteria.getStatus()));
    }
    if (criteria.getSeatId() != null) {
      builder.and(ticket.seatId.eq(criteria.getSeatId()));
    }
    if (criteria.getSeatName() != null) {
      builder.and(ticket.seatName.eq(criteria.getSeatName()));
    }

    JPQLQuery<Ticket> query = queryFactory
        .selectFrom(ticket)
        .where(builder);

    // 페이징 처리
    QueryResults<Ticket> results = query
        .offset((long) criteria.getPage() * criteria.getSize())
        .limit(criteria.getSize())
        .fetchResults();

    return new PageImpl<>(results.getResults(),
        PageRequest.of(criteria.getPage(), criteria.getSize()), results.getTotal());
  }
}
