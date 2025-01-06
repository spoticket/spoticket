package com.spoticket.teamstadium.infrastructure.repository.queryDsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spoticket.teamstadium.domain.model.QStadium;
import com.spoticket.teamstadium.domain.model.Stadium;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StadiumRepositoryCustomImpl implements StadiumRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Stadium> searchByKeyword(String keyword) {
    QStadium stadium = QStadium.stadium;

    return queryFactory.selectFrom(stadium)
        .where(stadium.isDeleted.isFalse()
            .and(
                stadium.name.containsIgnoreCase(keyword)
                    .or(stadium.address.containsIgnoreCase(keyword))
            )
        )
        .fetch();
  }
}
