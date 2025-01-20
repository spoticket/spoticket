//package com.spoticket.payment;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import com.spoticket.payment.infrastrucutre.toss.dto.TossCancelPaymentReq;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//@SpringBootTest
//class PaymentControllerTest {
//
//    @LocalServerPort
//    private int port = 8084;
//
//    @Test
//    void cancelPayment() {
//        // given
//        String paymentKey = "test_key123";  // 실제 존재하는 paymentKey 사용
//        TossCancelPaymentReq request = new TossCancelPaymentReq("tviva2025010901295657yL3","고객 변심");
//
//        String url = "http://localhost:" + port + "/api/v1/payments/" + paymentKey + "/cancel";
//
//        // when
//        ResponseEntity<String> response = new RestTemplate().postForEntity(
//            url,
//            request,
//            String.class
//        );
//
//        // then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        System.out.println("Response: " + response.getBody());
//    }
//}