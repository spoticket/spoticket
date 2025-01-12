package com.spoticket.payment.domain.payment.service;

import com.spoticket.payment.domain.payment.model.PaymentStatus;
import com.spoticket.payment.domain.payment.model.Payments;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentDomainService {

    public void updatePaymentStatus(Payments payment, String tossStatus, String paymentKey) {
        PaymentStatus newStatus = PaymentStatus.fromTossStatus(tossStatus);
        if (newStatus == PaymentStatus.COMPLETED) {
            payment.updateToComplete(paymentKey);
        } else {
            throw new IllegalStateException("상태 변경 불가");
        }
    }

    public void updatedPaymentStatusByCancel(Payments payment, String tossStatus) {
        PaymentStatus newStatus = PaymentStatus.fromTossStatus(tossStatus);

        if (newStatus == PaymentStatus.CANCELLED) {
            payment.updateToCancel();
        }
    }
}