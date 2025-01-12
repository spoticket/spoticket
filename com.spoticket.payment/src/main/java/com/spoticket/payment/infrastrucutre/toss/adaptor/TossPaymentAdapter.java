package com.spoticket.payment.infrastrucutre.toss.adaptor;

import com.spoticket.payment.domain.payment.model.CardCompany;
import com.spoticket.payment.domain.payment.port.TossPaymentPort;
import com.spoticket.payment.infrastrucutre.toss.TossClient;
import com.spoticket.payment.infrastrucutre.toss.dto.TossPaymentReq;
import com.spoticket.payment.infrastrucutre.toss.dto.TossPaymentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TossPaymentAdapter implements TossPaymentPort {
    private final TossClient tossClient;
    
    @Override
    public TossPaymentRes confirmPayment(TossPaymentReq paymentReq) {
        return tossClient.confirmPayment(paymentReq);
    }

    @Override
    public TossPaymentRes cancelPayment(String paymentKey, String cancelReason) {
        return tossClient.cancelPayment(paymentKey, cancelReason);
    }

    @Override
    public String getCardCompanyName(String issuerCode) {
        return CardCompany.getShortNameByCode(issuerCode);
    }


}