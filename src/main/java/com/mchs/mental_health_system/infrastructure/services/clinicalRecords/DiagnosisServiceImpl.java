package com.mchs.mental_health_system.infrastructure.services.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisResponseDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.ClinicalRecordsMapper;
import com.mchs.mental_health_system.application.services.clinicalRecords.DiagnosisService;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Diagnosis;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.DiagnosisRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.exceptions.patient.PatientNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final PatientRepository patientRepository;
    private final ClinicalRecordsMapper clinicalRecordsMapper;

    @Override
    @Transactional
    public DiagnosisResponseDTO addDiagnosis(Long patientId, DiagnosisRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        Diagnosis newDiagnosis = clinicalRecordsMapper.toEntity(requestDTO);
        newDiagnosis.setPatient(patient);

        if (newDiagnosis.isPrimary()) {
            unsetCurrentPrimaryDiagnosis(patient);
        }

        Diagnosis savedDiagnosis = diagnosisRepository.save(newDiagnosis);
        return clinicalRecordsMapper.toResponseDTO(savedDiagnosis);
    }

    @Override
    @Transactional
    public DiagnosisResponseDTO setAsPrimaryDiagnosis(Long diagnosisId) {
        Diagnosis diagnosisToSet = findDiagnosisByIdOrThrow(diagnosisId);

        if (diagnosisToSet.isPrimary()) {
            return clinicalRecordsMapper.toResponseDTO(diagnosisToSet);
        }

        unsetCurrentPrimaryDiagnosis(diagnosisToSet.getPatient());

        diagnosisToSet.setPrimary(true);
        Diagnosis updatedDiagnosis = diagnosisRepository.save(diagnosisToSet);

        return clinicalRecordsMapper.toResponseDTO(updatedDiagnosis);
    }

    @Override
    public List<DiagnosisResponseDTO> getDiagnosesForPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException(patientId);
        }

        List<Diagnosis> diagnoses = diagnosisRepository.findByPatientId(patientId);
        return clinicalRecordsMapper.toDiagnosisResponseDTOList(diagnoses);
    }

    private void unsetCurrentPrimaryDiagnosis(Patient patient) {
        diagnosisRepository.findByPatientIdAndPrimaryTrue(patient.getId()).ifPresent(currentPrimary -> {
            currentPrimary.setPrimary(false);
            diagnosisRepository.save(currentPrimary);
        });
    }

    private Diagnosis findDiagnosisByIdOrThrow(Long diagnosisId) {
        return diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found with ID: " + diagnosisId));
    }
}
