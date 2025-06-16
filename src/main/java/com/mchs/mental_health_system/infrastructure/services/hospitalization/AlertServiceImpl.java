package com.mchs.mental_health_system.infrastructure.services.hospitalization;

import com.mchs.mental_health_system.application.dto.hospitalization.AlertRequestDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.AlertResponseDTO;
import com.mchs.mental_health_system.application.mappers.hospitalization.AlertMapper;
import com.mchs.mental_health_system.application.services.hospitalization.AlertService;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.Alert;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.enums.hospitalization.AlertStatus;
import com.mchs.mental_health_system.domain.repositories.hospitalization.AlertRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.domain.repositories.user.HealthProfessionalRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.exceptions.patient.PatientNotFoundException;
import com.mchs.mental_health_system.exceptions.user.ProfessionalNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final PatientRepository patientRepository;
    private final HealthProfessionalRepository healthProfessionalRepository;
    private final AlertMapper alertMapper;

    @Override
    @Transactional
    public AlertResponseDTO createAlert(Long patientId, AlertRequestDTO requestDTO) {
        Patient patient = findPatientByIdOrThrow(patientId);
        Alert newAlert = new Alert();
        newAlert.setPatient(patient);
        newAlert.setType(requestDTO.type());
        newAlert.setMessage(requestDTO.message());
        if (requestDTO.assignedToId() != null) {
            HealthProfessional professional = findProfessionalByIdOrThrow(requestDTO.assignedToId());
            newAlert.setAssignedTo(professional);
        }
        Alert savedAlert = alertRepository.save(newAlert);
        return alertMapper.toResponseDTO(savedAlert);
    }

    @Override
    @Transactional
    public AlertResponseDTO assignAlert(Long alertId, Long professionalId) {
        Alert alert = findAlertByIdOrThrow(alertId);
        if (alert.getStatus() != AlertStatus.OPEN) {
            throw new BusinessException("Cannot assign an alert that is not in OPEN status. Current status: " + alert.getStatus());
        }
        HealthProfessional professional = findProfessionalByIdOrThrow(professionalId);
        alert.setAssignedTo(professional);
        Alert updatedAlert = alertRepository.save(alert);
        return alertMapper.toResponseDTO(updatedAlert);
    }

    @Override
    @Transactional
    public AlertResponseDTO resolveAlert(Long alertId) {
        Alert alert = findAlertByIdOrThrow(alertId);
        if (alert.getStatus() == AlertStatus.RESOLVED) {
            return alertMapper.toResponseDTO(alert);
        }
        alert.setStatus(AlertStatus.RESOLVED);
        alert.setResolvedAt(LocalDateTime.now());
        Alert updatedAlert = alertRepository.save(alert);
        return alertMapper.toResponseDTO(updatedAlert);
    }

    @Override
    public List<AlertResponseDTO> listOpenAlerts() {
        return alertRepository.findByStatus(AlertStatus.OPEN).stream()
                .map(alertMapper::toResponseDTO)
                .toList();
    }

    private Alert findAlertByIdOrThrow(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with ID: " + id));
    }

    private HealthProfessional findProfessionalByIdOrThrow(Long id) {
        return healthProfessionalRepository.findById(id)
                .orElseThrow(() -> new ProfessionalNotFoundException(id));
    }

    private Patient findPatientByIdOrThrow(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }
}
