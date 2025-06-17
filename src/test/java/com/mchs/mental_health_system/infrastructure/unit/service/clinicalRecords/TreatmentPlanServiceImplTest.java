package com.mchs.mental_health_system.infrastructure.unit.service.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.TreatmentPlanRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.TreatmentPlanResponseDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.ClinicalRecordsMapper;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.TreatmentPlan;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.TreatmentPlanStatus;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.TreatmentPlanRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.domain.repositories.user.HealthProfessionalRepository;
import com.mchs.mental_health_system.exceptions.clinicalRecords.DuplicateTreatmentPlanException;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.exceptions.patient.PatientNotFoundException;
import com.mchs.mental_health_system.exceptions.user.ProfessionalNotFoundException;
import com.mchs.mental_health_system.infrastructure.services.clinicalRecords.TreatmentPlanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentPlanServiceImplTest {

    @Mock
    private TreatmentPlanRepository treatmentPlanRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private HealthProfessionalRepository healthProfessionalRepository;

    @Mock
    private ClinicalRecordsMapper clinicalRecordsMapper;

    @InjectMocks
    private TreatmentPlanServiceImpl treatmentPlanService;

    private Patient patient;
    private HealthProfessional supervisor;
    private TreatmentPlan treatmentPlan;
    private TreatmentPlanRequestDTO requestDTO;
    private TreatmentPlanResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);

        supervisor = new HealthProfessional();
        supervisor.setId(1L);

        requestDTO = new TreatmentPlanRequestDTO(
                1L,
                TreatmentPlanStatus.ACTIVE,
                "Objectives",
                LocalDate.now(),
                LocalDate.now().plusMonths(6)
        );

        treatmentPlan = new TreatmentPlan();
        treatmentPlan.setId(1L);
        treatmentPlan.setPatient(patient);
        treatmentPlan.setSupervisor(supervisor);
        treatmentPlan.setStatus(TreatmentPlanStatus.ACTIVE);

        responseDTO = new TreatmentPlanResponseDTO(
                1L,
                TreatmentPlanStatus.ACTIVE,
                "Definir objetivos claros e alcançáveis.",
                LocalDate.now(),
                LocalDate.now().plusMonths(6),
                1L,
                "Nome do Paciente",
                1L,
                "Nome do Supervisor"
        );
    }

    @Test
    @DisplayName("createTreatmentPlan should return DTO when successful")
    void createTreatmentPlan_shouldReturnDto_whenSuccessful() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(treatmentPlanRepository.findByPatientId(1L)).thenReturn(Optional.empty());
        when(healthProfessionalRepository.findById(1L)).thenReturn(Optional.of(supervisor));
        when(clinicalRecordsMapper.toEntity(any(TreatmentPlanRequestDTO.class))).thenReturn(new TreatmentPlan());
        when(treatmentPlanRepository.save(any(TreatmentPlan.class))).thenReturn(treatmentPlan);
        when(clinicalRecordsMapper.toResponseDTO(any(TreatmentPlan.class))).thenReturn(responseDTO);

        TreatmentPlanResponseDTO result = treatmentPlanService.createTreatmentPlan(1L, requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.patientId()).isEqualTo(patient.getId());
        verify(treatmentPlanRepository).save(any(TreatmentPlan.class));
    }

    @Test
    @DisplayName("createTreatmentPlan should throw PatientNotFoundException when patient does not exist")
    void createTreatmentPlan_shouldThrowException_whenPatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> {
            treatmentPlanService.createTreatmentPlan(1L, requestDTO);
        });

        verify(treatmentPlanRepository, never()).save(any());
    }

    @Test
    @DisplayName("createTreatmentPlan should throw DuplicateTreatmentPlanException when plan already exists")
    void createTreatmentPlan_shouldThrowException_whenPlanAlreadyExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(treatmentPlanRepository.findByPatientId(1L)).thenReturn(Optional.of(treatmentPlan));

        assertThrows(DuplicateTreatmentPlanException.class, () -> {
            treatmentPlanService.createTreatmentPlan(1L, requestDTO);
        });

        verify(treatmentPlanRepository, never()).save(any());
    }

    @Test
    @DisplayName("createTreatmentPlan should throw ProfessionalNotFoundException when supervisor does not exist")
    void createTreatmentPlan_shouldThrowException_whenSupervisorNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(treatmentPlanRepository.findByPatientId(1L)).thenReturn(Optional.empty());
        when(healthProfessionalRepository.findById(1L)).thenReturn(Optional.empty());
        when(clinicalRecordsMapper.toEntity(any(TreatmentPlanRequestDTO.class))).thenReturn(new TreatmentPlan());

        assertThrows(ProfessionalNotFoundException.class, () -> {
            treatmentPlanService.createTreatmentPlan(1L, requestDTO);
        });

        verify(treatmentPlanRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateTreatmentPlanStatus should return DTO with updated status")
    void updateTreatmentPlanStatus_shouldReturnDto_whenSuccessful() {
        TreatmentPlan planForUpdate = new TreatmentPlan();
        planForUpdate.setStatus(TreatmentPlanStatus.ACTIVE);

        when(treatmentPlanRepository.findById(1L)).thenReturn(Optional.of(planForUpdate));
        when(treatmentPlanRepository.save(any(TreatmentPlan.class))).thenReturn(planForUpdate);
        when(clinicalRecordsMapper.toResponseDTO(any(TreatmentPlan.class))).thenReturn(responseDTO);

        treatmentPlanService.updateTreatmentPlanStatus(1L, TreatmentPlanStatus.COMPLETED);

        verify(treatmentPlanRepository).save(planForUpdate);
        assertThat(planForUpdate.getStatus()).isEqualTo(TreatmentPlanStatus.COMPLETED);
    }

    @Test
    @DisplayName("updateTreatmentPlanStatus should throw ResourceNotFoundException when plan does not exist")
    void updateTreatmentPlanStatus_shouldThrowException_whenPlanNotFound() {
        when(treatmentPlanRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            treatmentPlanService.updateTreatmentPlanStatus(1L, TreatmentPlanStatus.COMPLETED);
        });
    }

    @Test
    @DisplayName("getTreatmentPlanForPatient should return DTO when plan exists")
    void getTreatmentPlanForPatient_shouldReturnDto_whenPlanExists() {
        when(treatmentPlanRepository.findByPatientId(1L)).thenReturn(Optional.of(treatmentPlan));
        when(clinicalRecordsMapper.toResponseDTO(treatmentPlan)).thenReturn(responseDTO);

        TreatmentPlanResponseDTO result = treatmentPlanService.getTreatmentPlanForPatient(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(treatmentPlan.getId());
    }

    @Test
    @DisplayName("getTreatmentPlanForPatient should throw ResourceNotFoundException when plan does not exist")
    void getTreatmentPlanForPatient_shouldThrowException_whenPlanNotFound() {
        when(treatmentPlanRepository.findByPatientId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            treatmentPlanService.getTreatmentPlanForPatient(1L);
        });
    }
}