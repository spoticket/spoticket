package com.spoticket.teamstadium.domain.model;

import com.spoticket.teamstadium.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.locationtech.jts.geom.Point;

@Entity(name = "P_STADIUM")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Stadium extends BaseEntity {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(updatable = false, nullable = false)
  private UUID stadiumId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private Point latLng;

  private String seatImage;

  private String description;

  @OneToMany(mappedBy = "stadium", fetch = FetchType.LAZY)
  private List<Seat> seats = new ArrayList<>();

  public static Stadium create(
      String name,
      String address,
      Point latLng,
      String seatImage,
      String description
  ) {
    return Stadium.builder()
        .name(name)
        .address(address)
        .latLng(latLng)
        .seatImage(seatImage)
        .description(description)
        .build();
  }

  public void update(
      String name,
      String address,
      String seatImage,
      String description,
      Point latLng
  ) {
    if (name != null) {
      this.name = name;
    }
    if (address != null) {
      this.address = address;
    }
    if (seatImage != null) {
      this.seatImage = seatImage;
    }
    if (description != null) {
      this.description = description;
    }
    if (latLng != null) {
      this.latLng = latLng;
    }
  }
}
