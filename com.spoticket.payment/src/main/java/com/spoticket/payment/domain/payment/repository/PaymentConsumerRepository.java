package com.spoticket.payment.domain.payment.repository;

import com.spoticket.payment.domain.payment.model.PaymentConsumer;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentConsumerRepository extends JpaRepository<PaymentConsumer, UUID> {

    Optional<PaymentConsumer> findByOrderId(UUID consumerId);
}
