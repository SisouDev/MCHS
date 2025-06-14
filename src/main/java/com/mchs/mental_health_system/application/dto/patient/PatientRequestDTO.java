package com.mchs.mental_health_system.application.dto.patient;

import com.mchs.mental_health_system.domain.model.shared.embeddable.EmergencyContact;
import com.mchs.mental_health_system.domain.model.shared.embeddable.PersonalData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record PatientRequestDTO(
        @NotNull @Valid
        PersonalData personalData,

        @NotNull @Valid
        EmergencyContact emergencyContact
) {
}
