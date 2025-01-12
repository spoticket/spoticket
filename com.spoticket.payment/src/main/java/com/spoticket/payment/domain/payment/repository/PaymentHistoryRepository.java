package com.spoticket.payment.domain.payment.repository;

import com.spoticket.payment.domain.payment.model.PaymentHistories;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistories, UUID> {

}
