package com.mchs.mental_health_system.domain.repositories.clinicalRecords;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.TreatmentPlan;
import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.TreatmentPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {

    Optional<TreatmentPlan> findByPatientId(Long patientId);

    List<TreatmentPlan> findBySupervisorId(Long supervisorId);

    List<TreatmentPlan> findByStatus(TreatmentPlanStatus status);
}