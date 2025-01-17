package com.spoticket.game.application.dto.response;

public record ReadLeagueRankResponse(

    String name,
    int season,
    GenericPagedModel<ReadLeagueRankListResponse> ranking
) {

}
