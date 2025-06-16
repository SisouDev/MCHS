package com.mchs.mental_health_system.application.dto.patient;

import com.mchs.mental_health_system.domain.model.shared.embeddable.EmergencyContact;
import com.mchs.mental_health_system.domain.model.shared.embeddable.PersonalData;

public record PatientResponseDTO(
        Long id,
        PersonalData personalData,
        EmergencyContact emergencyContact,
        Long careFacilityId
) {
}
