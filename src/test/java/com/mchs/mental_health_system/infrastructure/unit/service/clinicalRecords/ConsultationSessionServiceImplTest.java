package com.mchs.mental_health_system.infrastructure.unit.service.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PrescriptionRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PrescriptionResponseDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.ClinicalRecordsMapper;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.ConsultationSession;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Prescription;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.ConsultationSessionRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.domain.repositories.user.HealthProfessionalRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.infrastructure.services.clinicalRecords.ConsultationSessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationSessionServiceImplTest {

    @Mock
    private ConsultationSessionRepository consultationSessionRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private HealthProfessionalRepository healthProfessionalRepository;
    @Mock
    private ClinicalRecordsMapper clinicalRecordsMapper;
    @InjectMocks
    private ConsultationSessionServiceImpl consultationSessionService;

    private Patient patient;
    private HealthProfessional professional;
    private ConsultationSession session;
    private ConsultationRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);

        professional = new HealthProfessional();
        professional.setId(1L);

        session = new ConsultationSession();
        session.setId(1L);
        session.setPatient(patient);
        session.setHealthProfessional(professional);
        session.setSessionDateTime(LocalDateTime.now().plusDays(1));
        session.setPrescriptions(new ArrayList<>());

        requestDTO = new ConsultationRequestDTO(1L, 1L, LocalDateTime.now().plusDays(1), "");
    }

    @Test
    @DisplayName("scheduleSession deve agendar uma sessão com sucesso")
    void scheduleSession_shouldScheduleSuccessfully() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(healthProfessionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        when(clinicalRecordsMapper.toEntity(any(ConsultationRequestDTO.class))).thenReturn(session);
        when(consultationSessionRepository.save(any(ConsultationSession.class))).thenReturn(session);

        consultationSessionService.scheduleSession(requestDTO);

        verify(consultationSessionRepository).save(session);
    }

    @Test
    @DisplayName("scheduleSession deve lançar exceção ao agendar no passado")
    void scheduleSession_shouldThrowException_whenDateIsInThePast() {
        ConsultationRequestDTO pastRequest = new ConsultationRequestDTO(1L, 1L, LocalDateTime.now().minusDays(1), "");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(healthProfessionalRepository.findById(1L)).thenReturn(Optional.of(professional));

        assertThatThrownBy(() -> consultationSessionService.scheduleSession(pastRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot schedule a consultation session in the past.");
    }

    @Test
    @DisplayName("addClinicalNotes deve adicionar notas a uma sessão existente")
    void addClinicalNotes_shouldAddNotesSuccessfully() {
        String notes = "Patient reported feeling better.";
        when(consultationSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(consultationSessionRepository.save(any(ConsultationSession.class))).thenReturn(session);

        consultationSessionService.addClinicalNotes(1L, notes);

        assertThat(session.getClinicalNotes()).isEqualTo(notes);
        verify(consultationSessionRepository).save(session);
    }

    @Test
    @DisplayName("addClinicalNotes deve lançar exceção se a sessão não for encontrada")
    void addClinicalNotes_shouldThrowException_whenSessionNotFound() {
        when(consultationSessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultationSessionService.addClinicalNotes(1L, "some notes"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("findSessionsByPeriod deve retornar uma lista de sessões")
    void findSessionsByPeriod_shouldReturnSessionList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(7);
        when(consultationSessionRepository.findBySessionDateTimeBetween(start, end)).thenReturn(List.of(session));

        consultationSessionService.findSessionsByPeriod(start, end);

        verify(clinicalRecordsMapper, times(1)).toResponseDTO(any(ConsultationSession.class));
    }

    @Test
    @DisplayName("addPrescription deve adicionar prescrição a uma sessão com sucesso")
    void addPrescription_shouldAddPrescriptionSuccessfully() {
        PrescriptionRequestDTO prescriptionDTO = new PrescriptionRequestDTO("Medication A", "10mg", "Once a day", LocalDate.now(), 30);
        Prescription prescription = new Prescription();
        PrescriptionResponseDTO prescriptionResponse = new PrescriptionResponseDTO(1L, 1L, "Medication A", "10mg", "Once a day", LocalDate.now(), 30);

        when(consultationSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(clinicalRecordsMapper.toEntity(prescriptionDTO)).thenReturn(prescription);
        when(clinicalRecordsMapper.toResponseDTO(prescription)).thenReturn(prescriptionResponse);

        PrescriptionResponseDTO result = consultationSessionService.addPrescription(1L, prescriptionDTO);

        assertThat(session.getPrescriptions().size()).isEqualTo(1);
        assertThat(session.getPrescriptions().getFirst()).isEqualTo(prescription);
        assertThat(prescription.getConsultationSession()).isEqualTo(session);
        assertThat(result).isEqualTo(prescriptionResponse);
        verify(consultationSessionRepository).save(session);
    }
}