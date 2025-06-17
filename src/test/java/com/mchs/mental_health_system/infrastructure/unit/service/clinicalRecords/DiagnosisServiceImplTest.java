package com.mchs.mental_health_system.infrastructure.unit.service.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisResponseDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.ClinicalRecordsMapper;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Diagnosis;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.DiagnosisRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.exceptions.patient.PatientNotFoundException;
import com.mchs.mental_health_system.infrastructure.services.clinicalRecords.DiagnosisServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiagnosisServiceImplTest {

    @Mock
    private DiagnosisRepository diagnosisRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private ClinicalRecordsMapper clinicalRecordsMapper;
    @InjectMocks
    private DiagnosisServiceImpl diagnosisService;

    private Patient patient;
    private Diagnosis existingPrimaryDiagnosis;
    private Diagnosis newDiagnosis;
    private DiagnosisRequestDTO requestDTO;
    private DiagnosisResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);

        existingPrimaryDiagnosis = new Diagnosis();
        existingPrimaryDiagnosis.setId(10L);
        existingPrimaryDiagnosis.setPatient(patient);
        existingPrimaryDiagnosis.setPrimary(true);

        newDiagnosis = new Diagnosis();
        newDiagnosis.setId(11L);
        newDiagnosis.setPatient(patient);
        newDiagnosis.setPrimary(false);

        requestDTO = new DiagnosisRequestDTO("F32.2", "Depressão", LocalDate.now(), false);
        responseDTO = new DiagnosisResponseDTO(11L, 1L, "F32.2", "Depressão", LocalDate.now(), false);
    }

    @Test
    @DisplayName("addDiagnosis deve adicionar diagnóstico e desmarcar antigo primário se necessário")
    void addDiagnosis_shouldAddAndUnsetOldPrimary_whenNewIsPrimary() {
        DiagnosisRequestDTO primaryRequest = new DiagnosisRequestDTO("F41.1", "Ansiedade", LocalDate.now(), true);
        newDiagnosis.setPrimary(true); // O novo a ser salvo será primário

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(clinicalRecordsMapper.toEntity(primaryRequest)).thenReturn(newDiagnosis);
        when(diagnosisRepository.findByPatientIdAndPrimaryTrue(1L)).thenReturn(Optional.of(existingPrimaryDiagnosis));
        when(diagnosisRepository.save(any(Diagnosis.class))).thenReturn(newDiagnosis);
        when(clinicalRecordsMapper.toResponseDTO(newDiagnosis)).thenReturn(responseDTO);

        diagnosisService.addDiagnosis(1L, primaryRequest);

        assertThat(existingPrimaryDiagnosis.isPrimary()).isFalse();
        verify(diagnosisRepository, times(2)).save(any(Diagnosis.class));
        verify(diagnosisRepository).save(existingPrimaryDiagnosis);
        verify(diagnosisRepository).save(newDiagnosis);
    }

    @Test
    @DisplayName("addDiagnosis deve lançar PatientNotFoundException se paciente não existir")
    void addDiagnosis_shouldThrowException_whenPatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> {
            diagnosisService.addDiagnosis(1L, requestDTO);
        });

        verify(diagnosisRepository, never()).save(any());
    }

    @Test
    @DisplayName("setAsPrimaryDiagnosis deve definir novo primário e desmarcar o antigo")
    void setAsPrimaryDiagnosis_shouldSetNewPrimaryAndUnsetOld() {
        when(diagnosisRepository.findById(11L)).thenReturn(Optional.of(newDiagnosis));
        when(diagnosisRepository.findByPatientIdAndPrimaryTrue(1L)).thenReturn(Optional.of(existingPrimaryDiagnosis));
        when(diagnosisRepository.save(any(Diagnosis.class))).thenReturn(newDiagnosis);

        diagnosisService.setAsPrimaryDiagnosis(11L);

        assertThat(existingPrimaryDiagnosis.isPrimary()).isFalse();
        assertThat(newDiagnosis.isPrimary()).isTrue();
        verify(diagnosisRepository, times(2)).save(any(Diagnosis.class));
    }

    @Test
    @DisplayName("setAsPrimaryDiagnosis não deve fazer nada se o diagnóstico já for primário")
    void setAsPrimaryDiagnosis_shouldDoNothing_whenAlreadyPrimary() {
        when(diagnosisRepository.findById(10L)).thenReturn(Optional.of(existingPrimaryDiagnosis));

        diagnosisService.setAsPrimaryDiagnosis(10L);

        verify(diagnosisRepository, never()).save(any());
    }

    @Test
    @DisplayName("setAsPrimaryDiagnosis deve lançar ResourceNotFoundException se diagnóstico não existir")
    void setAsPrimaryDiagnosis_shouldThrowException_whenDiagnosisNotFound() {
        when(diagnosisRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            diagnosisService.setAsPrimaryDiagnosis(99L);
        });
    }

    @Test
    @DisplayName("getDiagnosesForPatient deve retornar lista de diagnósticos")
    void getDiagnosesForPatient_shouldReturnDiagnosisList() {
        List<Diagnosis> diagnoses = List.of(existingPrimaryDiagnosis, newDiagnosis);
        List<DiagnosisResponseDTO> dtos = List.of(responseDTO);

        when(patientRepository.existsById(1L)).thenReturn(true);
        when(diagnosisRepository.findByPatientId(1L)).thenReturn(diagnoses);
        when(clinicalRecordsMapper.toDiagnosisResponseDTOList(diagnoses)).thenReturn(dtos);

        List<DiagnosisResponseDTO> result = diagnosisService.getDiagnosesForPatient(1L);

        assertThat(result).isEqualTo(dtos);
    }

    @Test
    @DisplayName("getDiagnosesForPatient deve retornar lista vazia se não houver diagnósticos")
    void getDiagnosesForPatient_shouldReturnEmptyList_whenNoDiagnoses() {
        when(patientRepository.existsById(1L)).thenReturn(true);
        when(diagnosisRepository.findByPatientId(1L)).thenReturn(Collections.emptyList());
        when(clinicalRecordsMapper.toDiagnosisResponseDTOList(anyList())).thenReturn(Collections.emptyList());

        List<DiagnosisResponseDTO> result = diagnosisService.getDiagnosesForPatient(1L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getDiagnosesForPatient deve lançar PatientNotFoundException se paciente não existir")
    void getDiagnosesForPatient_shouldThrowException_whenPatientNotFound() {
        when(patientRepository.existsById(1L)).thenReturn(false);

        assertThrows(PatientNotFoundException.class, () -> {
            diagnosisService.getDiagnosesForPatient(1L);
        });
    }
}