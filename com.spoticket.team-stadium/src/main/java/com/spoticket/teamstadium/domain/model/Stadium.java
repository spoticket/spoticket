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
import org.springframework.util.StringUtils;

@Entity(name = "p_stadiums")
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

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 200)
  private String address;

  @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
  private Point latLng;

  @Column(length = 500)
  private String seatImage;

  @Column(columnDefinition = "TEXT")
  private String description;

  @OneToMany(mappedBy = "stadium", fetch = FetchType.LAZY)
  @Builder.Default
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
    if (name != null && StringUtils.hasText(name) && name.length() <= 100) {
      this.name = name;
    }
    if (address != null && address.length() <= 200) {
      this.address = address;
    }
    if (seatImage != null && seatImage.length() <= 500) {
      this.seatImage = seatImage;
    }
    this.description = description;
    if (latLng != null) {
      this.latLng = latLng;
    }
  }
}
