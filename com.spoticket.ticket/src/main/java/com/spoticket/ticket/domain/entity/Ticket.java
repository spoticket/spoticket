package com.spoticket.ticket.domain.entity;

import com.spoticket.ticket.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_ticket")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class Ticket extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID ticketId;

  private UUID userId;

  private UUID gameId;

  private UUID stadiumId;

  private UUID seatId;

  private String seatName;

  @Enumerated(value = EnumType.STRING)
  private TicketStatus status;

  public static Ticket create(UUID userId, UUID gameId, UUID stadiumId, UUID seatId,
      String seatName) {
    return Ticket.builder()
        .userId(userId)
        .gameId(gameId)
        .stadiumId(stadiumId)
        .seatId(seatId)
        .seatName(seatName)
        .status(TicketStatus.PICKED)
        .build();
  }

  public void update(TicketStatus status) {
    this.status = status;
  }

}
