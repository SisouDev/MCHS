package com.mchs.mental_health_system.infrastructure.unit.service.hospitalization;

import com.mchs.mental_health_system.application.dto.hospitalization.AlertRequestDTO;
import com.mchs.mental_health_system.application.factory.hospitalization.AlertFactory;
import com.mchs.mental_health_system.application.mappers.hospitalization.AlertMapper;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.Alert;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.enums.hospitalization.AlertStatus;
import com.mchs.mental_health_system.domain.model.enums.hospitalization.AlertType;
import com.mchs.mental_health_system.domain.repositories.hospitalization.AlertRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.domain.repositories.user.HealthProfessionalRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.infrastructure.services.hospitalization.AlertServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceImplTest {

    @Mock
    private AlertRepository alertRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private HealthProfessionalRepository healthProfessionalRepository;
    @Mock
    private AlertMapper alertMapper;
    @Mock
    private AlertFactory alertFactory;
    @InjectMocks
    private AlertServiceImpl alertService;

    private Patient patient;
    private HealthProfessional professional;
    private Alert alert;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);

        professional = new HealthProfessional();
        professional.setId(10L);

        alert = new Alert();
        alert.setId(100L);
        alert.setPatient(patient);
        alert.setStatus(AlertStatus.OPEN);
    }

    @Test
    @DisplayName("createAlert deve criar um alerta com sucesso")
    void createAlert_shouldCreateAlertSuccessfully() {
        AlertRequestDTO requestDTO = new AlertRequestDTO(AlertType.CRITICAL_MISSED_APPOINTMENT, "Paciente apresenta risco de auto-agressão.", 10L);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(healthProfessionalRepository.findById(10L)).thenReturn(Optional.of(professional));
        when(alertFactory.create(patient, requestDTO.type(), requestDTO.message())).thenReturn(alert);
        when(alertRepository.save(alert)).thenReturn(alert);

        alertService.createAlert(1L, requestDTO);

        verify(alertRepository).save(alert);
        assertThat(alert.getAssignedTo()).isEqualTo(professional);
    }

    @Test
    @DisplayName("assignAlert deve atribuir um alerta aberto a um profissional")
    void assignAlert_shouldAssignOpenAlert() {
        when(alertRepository.findById(100L)).thenReturn(Optional.of(alert));
        when(healthProfessionalRepository.findById(10L)).thenReturn(Optional.of(professional));
        when(alertRepository.save(alert)).thenReturn(alert);

        alertService.assignAlert(100L, 10L);

        assertThat(alert.getAssignedTo()).isEqualTo(professional);
        verify(alertRepository).save(alert);
    }

    @Test
    @DisplayName("assignAlert deve lançar exceção se o alerta não estiver aberto")
    void assignAlert_shouldThrowException_whenAlertIsNotOpen() {
        alert.setStatus(AlertStatus.RESOLVED);
        when(alertRepository.findById(100L)).thenReturn(Optional.of(alert));

        assertThatThrownBy(() -> alertService.assignAlert(100L, 10L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot assign an alert that is not in OPEN status. Current status: " + alert.getStatus());
    }

    @Test
    @DisplayName("resolveAlert deve resolver um alerta aberto com sucesso")
    void resolveAlert_shouldResolveOpenAlert() {
        when(alertRepository.findById(100L)).thenReturn(Optional.of(alert));
        when(alertRepository.save(alert)).thenReturn(alert);

        alertService.resolveAlert(100L);

        assertThat(alert.getStatus()).isEqualTo(AlertStatus.RESOLVED);
        assertThat(alert.getResolvedAt()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now());
        verify(alertRepository).save(alert);
    }

    @Test
    @DisplayName("resolveAlert não deve fazer nada se o alerta já estiver resolvido")
    void resolveAlert_shouldDoNothing_whenAlreadyResolved() {
        alert.setStatus(AlertStatus.RESOLVED);
        when(alertRepository.findById(100L)).thenReturn(Optional.of(alert));

        alertService.resolveAlert(100L);

        verify(alertRepository, never()).save(any());
    }

    @Test
    @DisplayName("resolveAlert deve lançar exceção se o alerta não for encontrado")
    void resolveAlert_shouldThrowException_whenAlertNotFound() {
        when(alertRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> alertService.resolveAlert(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("listOpenAlerts deve retornar uma lista de alertas abertos")
    void listOpenAlerts_shouldReturnOpenAlerts() {
        Alert anotherOpenAlert = new Alert();
        when(alertRepository.findByStatus(AlertStatus.OPEN)).thenReturn(List.of(alert, anotherOpenAlert));

        alertService.listOpenAlerts();

        verify(alertRepository).findByStatus(AlertStatus.OPEN);
        verify(alertMapper, times(2)).toResponseDTO(any(Alert.class));
    }
}