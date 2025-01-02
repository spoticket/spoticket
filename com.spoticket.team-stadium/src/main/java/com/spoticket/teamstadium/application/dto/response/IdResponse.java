package com.spoticket.teamstadium.application.dto.response;

import java.util.UUID;

public record IdResponse(
    String key,
    UUID value
) {

  public static IdResponse of(String key, UUID value) {
    return new IdResponse(key, value);
  }
}
