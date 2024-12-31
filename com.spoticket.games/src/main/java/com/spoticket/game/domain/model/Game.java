package com.spoticket.game.domain.model;

import com.spoticket.game.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_games")
public class Game extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID gameId;

    private String title;

    private Timestamp startTime;

    @Enumerated(value = EnumType.STRING)
    private Sport sport;

    private String league;

    private UUID stadiumId;

    private UUID homeTeamId;

    private UUID awayTeamId;

}
