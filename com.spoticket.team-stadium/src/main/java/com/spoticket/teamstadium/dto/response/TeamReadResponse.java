package com.spoticket.teamstadium.dto.response;

import java.util.List;

public record TeamReadResponse(
    TeamInfoResponse team,
    List<GameReadResponse> games
) {

}
