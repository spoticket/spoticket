package com.spoticket.teamstadium.domain.repository;

import com.spoticket.teamstadium.domain.model.Stadium;
import java.util.Optional;

public interface StadiumRepository {

  Optional<Stadium> findByNameAndIsDeletedFalse(String name);

  Stadium save(Stadium stadium);
}
