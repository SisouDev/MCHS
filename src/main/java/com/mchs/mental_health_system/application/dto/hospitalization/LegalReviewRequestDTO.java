package com.mchs.mental_health_system.application.dto.hospitalization;

import com.mchs.mental_health_system.domain.model.enums.hospitalization.LegalReviewStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LegalReviewRequestDTO(
        @NotNull LocalDate reviewDate,
        @NotNull LegalReviewStatus status,
        String reviewerName,
        String notes
) {
}
