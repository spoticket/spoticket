package com.spoticket.ticket.domain.entity;

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
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID ticketId;

  private UUID userId;

  private UUID gameId;

  private UUID seatId;

  private String seatName;

  @Enumerated(value = EnumType.STRING)
  private Status status;

}
