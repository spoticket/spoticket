package com.spoticket.payment.domain.payment.port;

import com.spoticket.payment.domain.payment.model.CardCompany;
import com.spoticket.payment.infrastrucutre.toss.dto.TossPaymentReq;
import com.spoticket.payment.infrastrucutre.toss.dto.TossPaymentRes;

public interface TossPaymentPort {
    TossPaymentRes confirmPayment(TossPaymentReq paymentReq);
    TossPaymentRes cancelPayment(String paymentKey, String cancelReason);

    String getCardCompanyName(String issuerCode);
}
