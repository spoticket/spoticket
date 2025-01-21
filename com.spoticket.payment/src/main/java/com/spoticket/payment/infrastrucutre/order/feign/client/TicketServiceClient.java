package com.spoticket.payment.infrastrucutre.order.feign.client;

import com.spoticket.payment.infrastrucutre.order.feign.dto.TicketInfoResponse;
import com.spoticket.payment.presentation.common.ApiSuccessResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("ticket")
public interface TicketServiceClient {

    @GetMapping("/api/v1/tickets/{ticketId}")
    ApiSuccessResponse<TicketInfoResponse> getTicket(@PathVariable("ticketId") UUID ticketId);
}
