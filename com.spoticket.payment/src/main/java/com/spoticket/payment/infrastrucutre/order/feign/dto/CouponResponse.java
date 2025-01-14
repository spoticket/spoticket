package com.spoticket.payment.infrastrucutre.order.feign.dto;

import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponse {
   private UUID couponId;
   private String name; 
   private Date expirationAt;
   private Boolean isAble;
   private String type;
   private Double discountRate;
   private Long stock;
   private Boolean isActive;
}