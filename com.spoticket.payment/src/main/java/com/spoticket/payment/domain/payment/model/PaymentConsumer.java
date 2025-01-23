package com.spoticket.payment.domain.payment.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_payment_consumer", schema = "payment_service")
@Entity
public class  PaymentConsumer {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID orderId;
    private String itemName;
    private long amount;


    public static PaymentConsumer toEntity(UUID orderId, String itemName, long amount) {
        return PaymentConsumer.builder()
            .orderId(orderId)
            .itemName(itemName)
            .amount(amount)
            .build();
    }
}
