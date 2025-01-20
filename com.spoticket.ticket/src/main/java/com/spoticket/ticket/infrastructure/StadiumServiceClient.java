package com.spoticket.ticket.infrastructure;


import com.spoticket.ticket.application.dtos.response.SeatReadResponse;
import com.spoticket.ticket.application.dtos.response.StadiumReadResponse;
import com.spoticket.ticket.global.config.FeignConfig;
import com.spoticket.ticket.global.util.ApiResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "team-stadium", configuration = FeignConfig.class)
public interface StadiumServiceClient {

  @GetMapping("/api/v1/stadiums/{stadiumId}")
  ApiResponse<StadiumReadResponse> getStadiumInfo(@PathVariable UUID stadiumId);

  @GetMapping("/api/v1/stadiums/seats/{seatId}")
  ApiResponse<SeatReadResponse> getSeat(@PathVariable UUID seatId);

}
