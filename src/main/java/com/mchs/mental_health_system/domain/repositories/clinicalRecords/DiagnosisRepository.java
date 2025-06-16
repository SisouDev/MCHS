package com.mchs.mental_health_system.domain.repositories.clinicalRecords;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    List<Diagnosis> findByPatientId(Long patientId);

    Optional<Diagnosis> findByPatientIdAndPrimaryTrue(Long patientId);

    List<Diagnosis> findByDiagnosisCodeAndDiagnosisDateBetween(String diagnosisCode, LocalDate start, LocalDate end);

    List<Diagnosis> findAllByDiagnosisDateBetween(LocalDate start, LocalDate end);
}