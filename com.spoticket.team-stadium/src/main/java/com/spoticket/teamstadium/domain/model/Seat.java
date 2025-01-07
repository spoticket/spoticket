package com.spoticket.teamstadium.domain.model;

import com.spoticket.teamstadium.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.StringUtils;

@Entity(name = "p_seats")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Seat extends BaseEntity {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(updatable = false, nullable = false)
  private UUID seatId;

  @Column(nullable = false)
  private UUID gameId;

  @Column(nullable = false, length = 100)
  private String section;

  @Min(0)
  @Column(nullable = false)
  private Long quantity;

  @Min(0)
  @Column(nullable = false)
  private Integer price;

  @ManyToOne
  @JoinColumn(name = "stadium_id", nullable = false)
  private Stadium stadium;

  public static Seat create(
      UUID gameId,
      String section,
      long quantity,
      Integer price,
      Stadium stadium
  ) {
    return Seat.builder()
        .gameId(gameId)
        .section(section)
        .quantity(quantity)
        .price(price)
        .stadium(stadium)
        .build();
  }

  public void update(
      String section,
      Long quantity,
      Integer price
  ) {
    if (section != null && StringUtils.hasText(section) && section.length() <= 100) {
      this.section = section;
    }
    if (quantity != null) {
      this.quantity = quantity;
    }
    if (price != null) {
      this.price = price;
    }
  }
}
