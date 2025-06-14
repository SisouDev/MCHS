package com.mchs.mental_health_system.application.dto.patient;

import java.time.LocalDateTime;

public record ConsultationSummaryDTO(
        Long id,
        LocalDateTime sessionDateTime,
        String healthProfessionalName
) {
}
