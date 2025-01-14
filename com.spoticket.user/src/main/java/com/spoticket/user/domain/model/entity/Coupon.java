package com.spoticket.user.domain.model.entity;

import com.spoticket.user.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_coupons", schema = "user_service")
@Builder
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID couponId;

    private String name;
    private Date expirationAt;
    private Boolean isAble;
    private String type;
    private Double discountRate;
    private Long stock;
    private Boolean isActive;

    public static Coupon of(String name, Date expirationAt, Boolean isAble, String type, Double discountRate, Long stock, Boolean isActive) {
        return Coupon.builder()
                .name(name)
                .expirationAt(expirationAt)
                .isAble(isAble)
                .type(type)
                .discountRate(discountRate)
                .stock(stock)
                .isActive(isActive)
                .build();
    }

    public void update(String name, Date expirationAt, Boolean isAble, String type, Double discountRate, Long stock, Boolean isActive) {
        if (isNotBlank(name)) this.name = name;
        if (nonNull(expirationAt)) this.expirationAt = expirationAt;
        if (isAble != null) this.isAble = isAble;
        if (isNotBlank(type)) this.type = type;
        if (nonNull(discountRate)) this.discountRate = discountRate;
        if (nonNull(stock)) this.stock = stock;
        if (nonNull(isActive)) this.isActive = isActive;
    }

    public void issue() {
        this.stock -= 1;

        if (this.stock <= 0) this.isAble = false;
    }
}
