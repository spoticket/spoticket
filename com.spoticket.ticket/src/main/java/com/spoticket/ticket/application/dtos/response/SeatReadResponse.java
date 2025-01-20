package com.spoticket.ticket.application.dtos.response;

public record SeatReadResponse(
    String section,
    Long quantity,
    Integer price
) {

}
