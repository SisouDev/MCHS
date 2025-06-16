package com.mchs.mental_health_system.application.dto.facility;

public record FacilityOccupancyItem(
        Long facilityId,
        String facilityName,
        int patientCount,
        int healthProfessionalCount
) {
}
