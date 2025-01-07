package com.spoticket.teamstadium.infrastructure.repository.jpa;


import com.spoticket.teamstadium.domain.model.Stadium;
import com.spoticket.teamstadium.domain.repository.StadiumRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStadiumRepository extends StadiumRepository, JpaRepository<Stadium, UUID> {

  @Query("SELECT st FROM p_stadiums st WHERE REPLACE(st.name, ' ', '') = REPLACE(:name, ' ', '') AND st.isDeleted = false")
  Optional<Stadium> findByNameAndIsDeletedFalse(@Param("name") String name);
}
