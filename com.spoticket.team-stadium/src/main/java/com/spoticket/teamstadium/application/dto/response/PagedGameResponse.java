package com.spoticket.teamstadium.application.dto.response;

import java.util.List;

public record PagedGameResponse(
    List<GameReadResponse> content,
    PageInfo page
) {

  public record PageInfo(
      int size,
      int number,
      long totalElements,
      int totalPages
  ) {

  }
}