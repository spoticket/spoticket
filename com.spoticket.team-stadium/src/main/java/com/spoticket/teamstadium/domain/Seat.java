package com.spoticket.teamstadium.domain;


import com.spoticket.teamstadium.common.BaseEntity;
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
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity(name = "P_SEATS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Seat extends BaseEntity {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(updatable = false, nullable = false)
  private UUID seatId;

  @Column(nullable = false)
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
      String section,
      long quantity,
      Integer price,
      Stadium stadium
  ) {
    return Seat.builder()
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
    if (section != null) {
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
