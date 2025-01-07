package com.spoticket.teamstadium.infrastructure.repository.queryDsl;

import com.spoticket.teamstadium.domain.model.Stadium;
import java.util.List;

public interface StadiumRepositoryCustom {

  List<Stadium> searchByKeyword(String keyword);
}
