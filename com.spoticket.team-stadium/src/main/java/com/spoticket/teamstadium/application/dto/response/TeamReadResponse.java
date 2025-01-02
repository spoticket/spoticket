package com.spoticket.teamstadium.application.dto.response;

import java.util.List;

public record TeamReadResponse(
    TeamInfoResponse team,
    List<GameReadResponse> games
) {

}
