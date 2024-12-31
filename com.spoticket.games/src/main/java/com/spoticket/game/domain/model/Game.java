package com.spoticket.game.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_games")
@Builder
public class Game {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID gameId;

    private String title;

    private LocalDateTime startTime;

    @Enumerated(value = EnumType.STRING)
    private Sport sport;

    private String league;

    private UUID stadiumId;

    private UUID homeTeamId;

    private UUID awayTeamId;

    public static Game of(String title, LocalDateTime startTime, Sport sport, String league,
                          UUID stadiumId, UUID homeTeamId, UUID awayTeamId) {
        return Game.builder()
                .title(title)
                .startTime(startTime)
                .sport(sport)
                .league(league)
                .stadiumId(stadiumId)
                .homeTeamId(homeTeamId)
                .awayTeamId(awayTeamId)
                .build();
    }

}
