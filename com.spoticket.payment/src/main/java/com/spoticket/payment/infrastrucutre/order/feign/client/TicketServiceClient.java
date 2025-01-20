package com.spoticket.payment.infrastrucutre.order.feign.client;

import com.spoticket.payment.infrastrucutre.order.feign.dto.TicketInfoResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("ticket-service")
public interface TicketServiceClient {

    @GetMapping("api/v1/ticket/{ticketId}")
    TicketInfoResponse getTicket(@PathVariable("ticketId") UUID ticketId);
}
