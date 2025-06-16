package com.mchs.mental_health_system.infrastructure.services.user;

import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationResponseDTO;
import com.mchs.mental_health_system.application.services.clinicalRecords.ConsultationSessionService;
import com.mchs.mental_health_system.application.services.user.SchedulingService;
import com.mchs.mental_health_system.domain.model.entities.user.Availability;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.ConsultationSessionRepository;
import com.mchs.mental_health_system.domain.repositories.user.AvailabilityRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {

    private static final Duration CONSULTATION_DURATION = Duration.ofMinutes(50);
    private static final Duration SLOT_INTERVAL = Duration.ofMinutes(60);

    private final AvailabilityRepository availabilityRepository;
    private final ConsultationSessionRepository consultationSessionRepository;
    private final ConsultationSessionService consultationSessionService;

    @Override
    public List<LocalDateTime> findAvailableSlots(Long professionalId, LocalDate date) {
        List<Availability> workHours = availabilityRepository.findByHealthProfessionalIdAndDayOfWeek(professionalId, date.getDayOfWeek());
        var existingSessions = consultationSessionRepository.findByHealthProfessionalIdAndSessionDateTimeBetween(
                professionalId, date.atStartOfDay(), date.plusDays(1).atStartOfDay());

        List<LocalDateTime> availableSlots = new ArrayList<>();

        for (Availability availability : workHours) {
            LocalTime potentialSlotTime = availability.getStartTime();
            while (potentialSlotTime.plus(CONSULTATION_DURATION).isBefore(availability.getEndTime()) || potentialSlotTime.plus(CONSULTATION_DURATION).equals(availability.getEndTime())) {
                LocalDateTime potentialSlotDateTime = date.atTime(potentialSlotTime);

                boolean isSlotTaken = existingSessions.stream().anyMatch(session -> {
                    LocalDateTime sessionStart = session.getSessionDateTime();
                    LocalDateTime sessionEnd = sessionStart.plus(CONSULTATION_DURATION);
                    return potentialSlotDateTime.isBefore(sessionEnd) && potentialSlotDateTime.plus(CONSULTATION_DURATION).isAfter(sessionStart);
                });

                if (!isSlotTaken) {
                    availableSlots.add(potentialSlotDateTime);
                }

                potentialSlotTime = potentialSlotTime.plus(SLOT_INTERVAL);
            }
        }
        return availableSlots;
    }

    @Override
    public ConsultationResponseDTO bookAppointment(ConsultationRequestDTO requestDTO) {
        List<LocalDateTime> availableSlots = findAvailableSlots(requestDTO.healthProfessionalId(), requestDTO.sessionDateTime().toLocalDate());
        boolean isSlotAvailable = availableSlots.stream().anyMatch(slot -> slot.equals(requestDTO.sessionDateTime()));

        if (!isSlotAvailable) {
            throw new BusinessException("The requested time slot is not available.");
        }

        return consultationSessionService.scheduleSession(requestDTO);
    }
}