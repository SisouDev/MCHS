package com.mchs.mental_health_system.application.dto.patient;

import com.mchs.mental_health_system.domain.model.shared.embeddable.EmergencyContact;
import com.mchs.mental_health_system.domain.model.shared.embeddable.PersonalData;

import java.util.List;

public record PatientDetailDTO(
        Long id,
        PersonalData personalData,
        EmergencyContact emergencyContact,
        List<ConsultationSummaryDTO> recentConsultations
) {
}
