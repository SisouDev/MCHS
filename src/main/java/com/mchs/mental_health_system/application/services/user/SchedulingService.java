package com.mchs.mental_health_system.application.services.user;

import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SchedulingService {
    List<LocalDateTime> findAvailableSlots(Long professionalId, LocalDate date);
    ConsultationResponseDTO bookAppointment(ConsultationRequestDTO requestDTO);
}