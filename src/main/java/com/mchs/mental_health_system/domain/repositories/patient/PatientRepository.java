package com.mchs.mental_health_system.domain.repositories.patient;

import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPersonalDataDocumentNumber(String documentNumber);


    List<Patient> findByPersonalDataFirstNameContainingIgnoreCaseAndPersonalDataLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Patient> findByCareFacilityId(Long careFacilityId);

    boolean existsByPersonalData_Document_Number(String personalDataDocumentNumber);

    List<Patient> findByPersonalData_FirstNameContainingIgnoreCase(String firstName);

    List<Patient> findByPersonalData_LastNameContainingIgnoreCase(String lastName);

    List<Patient> findByPersonalData_FirstNameContainingIgnoreCaseAndPersonalData_LastNameContainingIgnoreCase(String firstName, String lastName);
}