package com.mchs.mental_health_system.application.services.patient;

import com.mchs.mental_health_system.application.dto.clinicalRecords.CrisisEventRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.MedicalHistoryEventRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PatientEventResponseDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientCreationDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientResponseDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientSummaryDTO;

import java.util.List;

public interface PatientService {
    PatientResponseDTO findById(Long id);

    PatientResponseDTO registerNewPatient(PatientCreationDTO creationDTO);

    List<PatientSummaryDTO> searchByName(String firstName, String lastName);

    List<PatientSummaryDTO> listByFacility(Long careFacilityId);

    PatientEventResponseDTO recordCrisisEvent(Long patientId, CrisisEventRequestDTO requestDTO);

    PatientEventResponseDTO recordMedicalHistoryEvent(Long patientId, MedicalHistoryEventRequestDTO requestDTO);
}
