package com.mchs.mental_health_system.application.services.clinicalRecords;
import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisResponseDTO;

import java.util.List;

public interface DiagnosisService {
    DiagnosisResponseDTO addDiagnosis(Long patientId, DiagnosisRequestDTO requestDTO);

    DiagnosisResponseDTO setAsPrimaryDiagnosis(Long diagnosisId);

    List<DiagnosisResponseDTO> getDiagnosesForPatient(Long patientId);
}