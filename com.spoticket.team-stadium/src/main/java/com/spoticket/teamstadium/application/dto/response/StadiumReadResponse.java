package com.spoticket.teamstadium.application.dto.response;

import java.util.List;

public record StadiumReadResponse(
    StadiumInfoResponse stadium,
    List<GameReadResponse> games
) {

}
