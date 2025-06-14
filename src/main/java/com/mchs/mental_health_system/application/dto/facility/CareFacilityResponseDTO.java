package com.mchs.mental_health_system.application.dto.facility;

import com.mchs.mental_health_system.domain.model.shared.embeddable.Address;

public record CareFacilityResponseDTO(
        Long id,
        String name,
        Address address,
        String primaryPhone,
        String primaryEmail,

        int patientCount,
        int healthProfessionalCount,
        int administrativeProfessionalCount
) {
}
