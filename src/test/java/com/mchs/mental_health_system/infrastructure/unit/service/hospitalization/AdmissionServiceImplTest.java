package com.mchs.mental_health_system.infrastructure.unit.service.hospitalization;

import com.mchs.mental_health_system.application.dto.hospitalization.AdmissionRequestDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.LegalReviewRequestDTO;
import com.mchs.mental_health_system.application.mappers.hospitalization.AdmissionMapper;
import com.mchs.mental_health_system.application.mappers.hospitalization.LegalReviewMapper;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.Admission;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.LegalReview;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.enums.hospitalization.AdmissionType;
import com.mchs.mental_health_system.domain.model.enums.hospitalization.LegalReviewStatus;
import com.mchs.mental_health_system.domain.repositories.hospitalization.AdmissionRepository;
import com.mchs.mental_health_system.domain.repositories.hospitalization.LegalReviewRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.infrastructure.services.hospitalization.AdmissionServiceImpl;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdmissionServiceImplTest {

    @Mock
    private AdmissionRepository admissionRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private LegalReviewRepository legalReviewRepository;
    @Mock
    private AdmissionMapper admissionMapper;
    @Mock
    private LegalReviewMapper legalReviewMapper;
    @InjectMocks
    private AdmissionServiceImpl admissionService;

    private Patient patient;
    private Admission admission;
    private AdmissionRequestDTO admissionRequestDTO;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);

        admission = new Admission();
        admission.setId(1L);
        admission.setPatient(patient);
        admission.setAdmissionDate(LocalDateTime.now().minusDays(5));
        admission.setLegalReviews(new ArrayList<>());

        admissionRequestDTO = new AdmissionRequestDTO(AdmissionType.INVOLUNTARY, LocalDateTime.now(), "Risco para si mesmo", "Ala A");
    }

    @Test
    @DisplayName("admitPatient deve internar um paciente com sucesso")
    void admitPatient_shouldAdmitPatientSuccessfully() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(admissionRepository.findActiveByPatientId(1L)).thenReturn(Optional.empty());
        when(admissionMapper.toEntity(admissionRequestDTO)).thenReturn(admission);
        when(admissionRepository.save(admission)).thenReturn(admission);

        admissionService.admitPatient(1L, admissionRequestDTO);

        verify(admissionRepository).save(admission);
    }

    @Test
    @DisplayName("admitPatient deve lançar exceção se o paciente já estiver internado")
    void admitPatient_shouldThrowException_whenPatientAlreadyAdmitted() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(admissionRepository.findActiveByPatientId(1L)).thenReturn(Optional.of(admission));

        assertThatThrownBy(() -> admissionService.admitPatient(1L, admissionRequestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Patient already has an active admission (ID: 1).");
    }

    @Test
    @DisplayName("dischargePatient deve dar alta a um paciente com sucesso")
    void dischargePatient_shouldDischargePatientSuccessfully() {
        LocalDateTime dischargeDate = LocalDateTime.now();
        when(admissionRepository.findById(1L)).thenReturn(Optional.of(admission));
        when(admissionRepository.save(admission)).thenReturn(admission);

        admissionService.dischargePatient(1L, dischargeDate);

        assertThat(admission.getDischargeDate()).isEqualTo(dischargeDate);
        verify(admissionRepository).save(admission);
    }

    @Test
    @DisplayName("dischargePatient deve lançar exceção se a data de alta for anterior à de internação")
    void dischargePatient_shouldThrowException_whenDischargeDateIsBeforeAdmissionDate() {
        LocalDateTime invalidDischargeDate = admission.getAdmissionDate().minusDays(1);
        when(admissionRepository.findById(1L)).thenReturn(Optional.of(admission));

        assertThatThrownBy(() -> admissionService.dischargePatient(1L, invalidDischargeDate))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Discharge date cannot be before the admission date.");
    }

    @Test
    @DisplayName("addLegalReviewToAdmission deve adicionar revisão com sucesso")
    void addLegalReviewToAdmission_shouldAddReviewSuccessfully() {
        LegalReviewRequestDTO reviewDTO = new LegalReviewRequestDTO(LocalDate.now(), LegalReviewStatus.APPROVED, "Juiz Teste", "OK");
        LegalReview review = new LegalReview();
        when(admissionRepository.findById(1L)).thenReturn(Optional.of(admission));
        when(legalReviewMapper.toEntity(reviewDTO)).thenReturn(review);

        admissionService.addLegalReviewToAdmission(1L, reviewDTO);

        assertThat(admission.getLegalReviews().size()).isEqualTo(1);
        assertThat(admission.getLegalReviews().getFirst()).isEqualTo(review);
        verify(admissionRepository).save(admission);
    }

    @Test
    @DisplayName("updateLegalReview deve atualizar uma revisão existente")
    void updateLegalReview_shouldUpdateReviewSuccessfully() {
        LegalReviewRequestDTO reviewDTO = new LegalReviewRequestDTO(LocalDate.now(), LegalReviewStatus.REJECTED, "Juiz Teste 2", "Não OK");
        LegalReview existingReview = new LegalReview();
        existingReview.setId(10L);
        existingReview.setAdmission(admission);
        when(legalReviewRepository.findById(10L)).thenReturn(Optional.of(existingReview));

        admissionService.updateLegalReview(10L, reviewDTO);

        assertThat(existingReview.getStatus()).isEqualTo(LegalReviewStatus.REJECTED);
        assertThat(existingReview.getReviewerNotes()).isEqualTo("Não OK");
        verify(legalReviewRepository).save(existingReview);
    }

    @Test
    @DisplayName("deleteLegalReview deve deletar uma revisão com sucesso")
    void deleteLegalReview_shouldDeleteReviewSuccessfully() {
        LegalReview reviewToDelete = new LegalReview();
        reviewToDelete.setId(10L);
        when(legalReviewRepository.findById(10L)).thenReturn(Optional.of(reviewToDelete));

        admissionService.deleteLegalReview(10L);

        verify(legalReviewRepository).delete(reviewToDelete);
    }

    @Test
    @DisplayName("deleteLegalReview deve lançar exceção se a revisão não for encontrada")
    void deleteLegalReview_shouldThrowException_whenReviewNotFound() {
        when(legalReviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> admissionService.deleteLegalReview(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}