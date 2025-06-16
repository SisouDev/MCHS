package com.mchs.mental_health_system.application.services.clinicalRecords;
import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationResponseDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PrescriptionRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PrescriptionResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultationSessionService {
    ConsultationResponseDTO scheduleSession(ConsultationRequestDTO requestDTO);

    ConsultationResponseDTO addClinicalNotes(Long sessionId, String notes);

    List<ConsultationResponseDTO> findSessionsByPeriod(LocalDateTime start, LocalDateTime end);

    PrescriptionResponseDTO addPrescription(Long sessionId, PrescriptionRequestDTO requestDTO);

}