package com.spoticket.teamstadium.dto.response;

import java.util.List;

public record StadiumReadResponse(
    StadiumInfoResponse stadium,
    List<GameReadResponse> games
) {

}
