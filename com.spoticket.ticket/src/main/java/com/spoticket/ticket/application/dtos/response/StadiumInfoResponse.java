package com.spoticket.ticket.application.dtos.response;

import java.util.UUID;

public record StadiumInfoResponse(
    UUID stadiumId,
    String name,
    String address,
    String seatImage,
    String description
) {

}
