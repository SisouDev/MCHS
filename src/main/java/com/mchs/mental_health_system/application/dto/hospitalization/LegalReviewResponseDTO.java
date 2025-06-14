package com.mchs.mental_health_system.application.dto.hospitalization;

import com.mchs.mental_health_system.domain.model.enums.hospitalization.LegalReviewStatus;

import java.time.LocalDate;

public record LegalReviewResponseDTO(
        Long id,
        Long admissionId,
        LocalDate reviewDate,
        LegalReviewStatus status,
        String reviewerNotes
) {
}
