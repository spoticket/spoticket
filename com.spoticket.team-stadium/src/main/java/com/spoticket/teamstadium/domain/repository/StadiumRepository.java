package com.spoticket.teamstadium.domain.repository;

import com.spoticket.teamstadium.domain.model.Stadium;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StadiumRepository {

  Optional<Stadium> findByNameAndIsDeletedFalse(String name);

  Stadium save(Stadium stadium);

  Optional<Stadium> findByStadiumIdAndIsDeletedFalse(UUID stadiumId);

  Page<Stadium> findAllByIsDeletedFalse(Pageable pageable);

  List<Stadium> searchByKeyword(String keyword);
}
