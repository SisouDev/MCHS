package com.mchs.mental_health_system.domain.repositories.clinicalRecords;

import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.CrisisEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrisisEventRepository extends JpaRepository<CrisisEvent, Long> {

}