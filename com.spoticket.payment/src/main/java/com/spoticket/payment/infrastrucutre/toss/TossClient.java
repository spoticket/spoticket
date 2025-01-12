package com.spoticket.payment.infrastrucutre.toss;

import com.spoticket.payment.infrastrucutre.toss.dto.TossPaymentReq;
import com.spoticket.payment.infrastrucutre.toss.dto.TossPaymentRes;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class TossClient {

    @Value("${toss.api.toss_secret}")
    private String secretKey;
    private final String tossURL = "https://api.tosspayments.com/v1/payments/confirm";
    private final String cancelTossURL = "https://api.tosspayments.com/v1/payments/";
    private RestClient restClient;

    @PostConstruct
    public void init() {
        this.restClient = RestClient.builder()
            .defaultHeader(HttpHeaders.AUTHORIZATION, createAuthHeader())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    private String createAuthHeader() {
        byte[] encodedBytes = Base64.getEncoder().encode((secretKey + ":").getBytes(
            StandardCharsets.UTF_8));
        return "Basic " + new String(encodedBytes);
    }

    public TossPaymentRes confirmPayment(TossPaymentReq paymentReq) {
        log.info("Payment confirmation request - orderId: {}", paymentReq.orderId());

        TossPaymentRes response = restClient.post()
            .uri(tossURL)
            .body(paymentReq)
            .retrieve()
            .body(TossPaymentRes.class);

        log.info("Payment confirmation successful - orderId: {}", paymentReq.orderId());
        return response;
    }

    public TossPaymentRes cancelPayment(String paymentKey, String cancelReason) {
        String authorization = Base64.getEncoder()
            .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        return restClient.post()
            .uri(cancelTossURL + "/{paymentKey}/cancel", paymentKey)
            .header(HttpHeaders.AUTHORIZATION, "Basic " + authorization)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(Map.of("cancelReason", cancelReason))
            .retrieve()
            .body(TossPaymentRes.class);
    }
}