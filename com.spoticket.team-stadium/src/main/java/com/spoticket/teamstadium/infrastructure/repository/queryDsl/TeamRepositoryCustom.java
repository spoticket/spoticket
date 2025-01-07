package com.spoticket.teamstadium.infrastructure.repository.queryDsl;

import com.spoticket.teamstadium.domain.model.Team;
import java.util.List;

public interface TeamRepositoryCustom {

  List<Team> searchByKeyword(String keyword);
}
