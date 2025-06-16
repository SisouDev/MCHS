package com.mchs.mental_health_system.domain.repositories.clinicalRecords;

import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.MedicalHistoryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalHistoryEventRepository extends JpaRepository<MedicalHistoryEvent, Long> {}