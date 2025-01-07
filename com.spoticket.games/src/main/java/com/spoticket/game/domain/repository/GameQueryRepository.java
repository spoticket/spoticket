package com.spoticket.game.domain.repository;

import static com.spoticket.game.domain.model.QGame.game;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.Sport;
import com.spoticket.game.domain.repository.support.Querydsl4RepositorySupport;
import com.spoticket.game.dto.request.SearchCondition;
import com.spoticket.game.dto.response.GameResponse;
import com.spoticket.game.dto.response.QGameResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public class GameQueryRepository extends Querydsl4RepositorySupport {

  public GameQueryRepository() {
    super(Game.class);
  }

  public Page<GameResponse> getGames(SearchCondition condition) {
    return applyPagination(condition.getPageable(), contentQuery -> contentQuery
            .select(new QGameResponse(game.gameId, game.title, game.startTime, game.sport, game.league,
                game.stadiumId, game.homeTeamId, game.awayTeamId))
            .from(game)
            .where(createSearchBooleanBuilder(condition)),
        countQuery -> countQuery
            .select(game.count())
            .from(game)
            .where(createSearchBooleanBuilder(condition))
    );
  }

  private BooleanBuilder createSearchBooleanBuilder(SearchCondition condition) {
    return titleContains(condition.getTitle())
        .and(sportEq(condition.getSport()))
        .and(leagueEq(condition.getLeague()))
        .and(stadiumIdEq(condition.getStadiumId()))
        .and(teamIdEq(condition.getTeamId()))
        .and(startTimeBetween(condition.getStartDateTime(), condition.getEndDateTime()));
  }

  private BooleanBuilder titleContains(String titleCond) {
    return nullSafeBuilder(() -> game.title.containsIgnoreCase(titleCond));
  }

  private BooleanBuilder sportEq(Sport sportCond) {
    return nullSafeBuilder(() -> game.sport.eq(sportCond));
  }

  private BooleanBuilder leagueEq(String leagueCond) {
    return nullSafeBuilder(() -> game.league.eq(leagueCond));
  }

  private BooleanBuilder stadiumIdEq(UUID stadiumIdCond) {
    return nullSafeBuilder(() -> game.stadiumId.eq(stadiumIdCond));
  }

  private BooleanBuilder teamIdEq(UUID teamIdCond) {
    return nullSafeBuilder(() ->
        game.homeTeamId.eq(teamIdCond).or(game.awayTeamId.eq(teamIdCond))
    );
  }

  private BooleanBuilder startTimeBetween(LocalDateTime start, LocalDateTime end) {
    return nullSafeBuilder(() -> {
      if (start != null && end != null) {
        return game.startTime.between(start, end);
      } else if (start != null) {
        return game.startTime.goe(start);
      } else if (end != null) {
        return game.startTime.loe(end);
      } else {
        return null;
      }
    });
  }

  private BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
    try {
      return new BooleanBuilder(f.get());
    } catch (Exception e) {
      return new BooleanBuilder();
    }
  }

}
