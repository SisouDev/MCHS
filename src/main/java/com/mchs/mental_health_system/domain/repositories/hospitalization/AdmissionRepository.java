package com.mchs.mental_health_system.domain.repositories.hospitalization;

import com.mchs.mental_health_system.domain.model.entities.hospitalization.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    List<Admission> findByPatient_IdOrderByAdmissionDateDesc(Long patientId);

    @Query("SELECT a FROM Admission a WHERE a.patient.id = :patientId AND a.dischargeDate IS NULL")
    Optional<Admission> findActiveByPatientId(Long patientId);
}