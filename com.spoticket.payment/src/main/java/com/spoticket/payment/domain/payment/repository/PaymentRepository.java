package com.spoticket.payment.domain.payment.repository;

import com.spoticket.payment.domain.payment.model.Payments;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payments, UUID> {
    Optional<Payments> findByOrderId(UUID orderId);
    Optional<Payments> findByPaymentKey(String paymentKey);

}
