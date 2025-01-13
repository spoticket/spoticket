package com.spoticket.payment.application.order.event;

import com.spoticket.payment.domain.order.model.Order;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderCreatedEvent {
   private UUID orderId;
   private Long amount;
   private String itemName;
   
   public static OrderCreatedEvent from(Order order) {
       return new OrderCreatedEvent(
           order.getOrderId(),
           order.getAmount(),
           order.getOrderItems().get(0).getItemName()
       );
   }
}