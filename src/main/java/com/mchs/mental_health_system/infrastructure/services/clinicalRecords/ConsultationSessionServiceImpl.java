package com.mchs.mental_health_system.infrastructure.services.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationResponseDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PrescriptionRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PrescriptionResponseDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.ClinicalRecordsMapper;
import com.mchs.mental_health_system.application.services.clinicalRecords.ConsultationSessionService;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.ConsultationSession;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Prescription;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.ConsultationSessionRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationSessionServiceImpl implements ConsultationSessionService {

    private final ConsultationSessionRepository consultationSessionRepository;
    private final PatientRepository patientRepository;
    private final HealthProfessionalRepository healthProfessionalRepository;
    private final ClinicalRecordsMapper clinicalRecordsMapper;

    @Override
    @Transactional
    public ConsultationResponseDTO scheduleSession(ConsultationRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(requestDTO.patientId())
                .orElseThrow(() -> new PatientNotFoundException(requestDTO.patientId()));

        HealthProfessional professional = healthProfessionalRepository.findById(requestDTO.healthProfessionalId())
                .orElseThrow(() -> new ProfessionalNotFoundException(requestDTO.healthProfessionalId()));

        if (requestDTO.sessionDateTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Cannot schedule a consultation session in the past.");
        }

        ConsultationSession newSession = clinicalRecordsMapper.toEntity(requestDTO);
        newSession.setPatient(patient);
        newSession.setHealthProfessional(professional);

        ConsultationSession savedSession = consultationSessionRepository.save(newSession);
        return clinicalRecordsMapper.toResponseDTO(savedSession);
    }

    @Override
    @Transactional
    public ConsultationResponseDTO addClinicalNotes(Long sessionId, String notes) {
        ConsultationSession session = findSessionByIdOrThrow(sessionId);
        session.setClinicalNotes(notes);

        ConsultationSession updatedSession = consultationSessionRepository.save(session);
        return clinicalRecordsMapper.toResponseDTO(updatedSession);
    }

    @Override
    public List<ConsultationResponseDTO> findSessionsByPeriod(LocalDateTime start, LocalDateTime end) {
        return consultationSessionRepository.findBySessionDateTimeBetween(start, end).stream()
                .map(clinicalRecordsMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PrescriptionResponseDTO addPrescription(Long sessionId, PrescriptionRequestDTO requestDTO) {
        ConsultationSession session = findSessionByIdOrThrow(sessionId);

        Prescription newPrescription = clinicalRecordsMapper.toEntity(requestDTO);
        newPrescription.setConsultationSession(session);

        session.getPrescriptions().add(newPrescription);
        consultationSessionRepository.save(session);

        return clinicalRecordsMapper.toResponseDTO(newPrescription);
    }

    private ConsultationSession findSessionByIdOrThrow(Long sessionId) {
        return consultationSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation Session not found with ID: " + sessionId));
    }
}
