package com.spoticket.game.dto.request;

import com.spoticket.game.domain.model.Sport;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class SearchCondition {

  private String title;
  private Sport sport;
  private String league;
  private UUID stadiumId;
  private UUID teamId;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private Pageable pageable;

}
