package com.mchs.mental_health_system.domain.repositories.facility;

import com.mchs.mental_health_system.application.dto.facility.FacilityOccupancyItem;
import com.mchs.mental_health_system.domain.model.entities.facility.CareFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareFacilityRepository extends JpaRepository<CareFacility, Long> {
    Optional<CareFacility> findByName(String name);

    List<CareFacility> findByNameContainingIgnoreCase(String nameFragment);

    List<CareFacility> findByAddressCityIgnoreCase(String city);

    List<CareFacility> findByNameContainingIgnoreCaseAndAddressCityIgnoreCase(String nameFragment, String city);

    @Query("""
            SELECT new com.mchs.mental_health_system.application.dto.facility.FacilityOccupancyItem(
                cf.id,
                cf.name,
                size(cf.patients),
                size(cf.healthProfessionals)
            )
            FROM CareFacility cf
            """)
    List<FacilityOccupancyItem> getFacilityOccupancyData();

}