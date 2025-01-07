package com.spoticket.teamstadium.infrastructure.repository.queryDsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spoticket.teamstadium.domain.model.QTeam;
import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Team> searchByKeyword(String keyword) {
    QTeam team = QTeam.team;

    return queryFactory.selectFrom(team)
        .where(team.isDeleted.isFalse()
            .and(
                team.name.containsIgnoreCase(keyword)
                    .or(team.category.in(getMatchingCategories(keyword)))
            )
        )
        .fetch();
  }

  private List<TeamCategoryEnum> getMatchingCategories(String keyword) {
    return Arrays.stream(TeamCategoryEnum.values())
        .filter(category -> category.getKoreanName().contains(keyword))
        .toList();
  }

}
