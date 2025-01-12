package com.spoticket.payment.application.payment.service;




import com.spoticket.payment.application.payment.dto.PaymentRes;
import com.spoticket.payment.domain.payment.exception.PaymentErrorCode;
import com.spoticket.payment.domain.payment.exception.PaymentException;
import com.spoticket.payment.domain.payment.model.PaymentHistories;
import com.spoticket.payment.domain.payment.model.PaymentStatus;
import com.spoticket.payment.domain.payment.model.Payments;
import com.spoticket.payment.domain.payment.port.TossPaymentPort;
import com.spoticket.payment.domain.payment.repository.PaymentHistoryRepository;
import com.spoticket.payment.domain.payment.repository.PaymentRepository;
import com.spoticket.payment.domain.payment.service.PaymentDomainService;
import com.spoticket.payment.infrastrucutre.toss.dto.TossPaymentReq;
import com.spoticket.payment.infrastrucutre.toss.dto.TossPaymentRes;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final TossPaymentPort paymentPort;
    private final PaymentDomainService paymentDomainService;



    @Transactional
    public void createOrReusePayment(UUID orderId, long amount) {
        if (isReusablePayment(orderId)) {
            createPayment(orderId, amount);
        }
    }
    public void createPayment(UUID orderId, long amount) {
        log.info("결제 생성 시작 - orderId: {}, amount: {}, timestamp: {}",
            orderId, amount, LocalDateTime.now());

        Payments payment = Payments.createPayments(
            orderId, amount
        );
        Payments savedPayment = paymentRepository.save(payment);
        log.info("결제 생성 성공 - paymentId: {}, orderId: {}, amount: {}, createdAt: {}",
            savedPayment.getPaymentId(),
            savedPayment.getOrderId(),
            savedPayment.getAmount(),
            savedPayment.getCreatedAt());
    }

    private boolean isReusablePayment(UUID orderId) {
        Optional<Payments> existingPayment = paymentRepository.findByOrderId(orderId);
        if (existingPayment.isEmpty()) {
            return true;
        }
        Payments payment = existingPayment.get();
        if (payment.getStatus() == PaymentStatus.PENDING) {
            log.info("기존 PENDING 상태 결제 재사용 - paymentId: {}, orderId: {}",
                payment.getPaymentId(), orderId);
            return false;
        }
        log.warn("이미 처리된 주문의 결제 요청 - orderId: {}, status: {}",
            orderId, payment.getStatus());
        throw new PaymentException(PaymentErrorCode.PAYMENT_ALREADY_PROCESSED);
    }


    @Transactional
    public TossPaymentRes confirmPayment(TossPaymentReq paymentReq) {
        Payments payment = paymentRepository.findByOrderId(paymentReq.orderId())
            .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND));
        TossPaymentRes paymentRes = paymentPort.confirmPayment(paymentReq);
        verifyAmount(paymentReq.amount(), paymentRes.totalAmount());
        paymentDomainService.updatePaymentStatus(
            payment,
            paymentRes.status(),
            paymentReq.paymentKey()
        );
        String cardCompanyName = convertCardCompanyName(paymentRes.card().issuerCode());
        paymentHistoryRepository.save(
            PaymentHistories.createPaymentStatusHistory(
                payment,
                paymentRes.method(),
                paymentRes.status(),
                paymentRes.description(),
                cardCompanyName,
                paymentRes.card().installmentPlanMonths()
            )
        );
        log.info("결제 승인 완료 응답 - paymentId: {}, method: {}, status: {}, description: {}, cardCompanyName: {}",
            payment.getPaymentId(),
            paymentRes.method(),
            paymentRes.status(),
            paymentRes.description(),
            cardCompanyName
        );
        return paymentRes;
    }
    public void verifyAmount(long reqAmount, long resAmount ) {
        if (reqAmount != resAmount) {
            throw new PaymentException(PaymentErrorCode.AMOUNT_NOT_EQUAL);
        }
    }
    @Transactional
    public TossPaymentRes cancelPayment(String paymentKey, String cancelReason) {
        Payments payment = paymentRepository.findByPaymentKey(paymentKey)
            .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND));

        TossPaymentRes paymentRes = paymentPort.cancelPayment(
            paymentKey,
            cancelReason
        );
        paymentDomainService.updatedPaymentStatusByCancel(
            payment,
            paymentRes.status()
        );

        String cardCompanyName = convertCardCompanyName(paymentRes.card().issuerCode());

        paymentHistoryRepository.save(
            PaymentHistories.createPaymentStatusHistory(
                payment,
                paymentRes.method(),
                paymentRes.status(),
                cancelReason,
                cardCompanyName,
                paymentRes.card().installmentPlanMonths())
        );
        return paymentRes;
    }

    private String convertCardCompanyName(String issuerCode) {
        return paymentPort.getCardCompanyName(issuerCode);
    }

    public PaymentRes getPayment(UUID paymentId) {
        return PaymentRes.from(paymentRepository.findById(paymentId)
            .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND)));

    }
}
