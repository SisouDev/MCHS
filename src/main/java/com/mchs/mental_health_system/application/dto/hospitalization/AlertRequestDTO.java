package com.mchs.mental_health_system.application.dto.hospitalization;

import com.mchs.mental_health_system.domain.model.enums.hospitalization.AlertType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlertRequestDTO(
        @NotNull(message = "Alert type is mandatory.")
        AlertType type,

        @NotBlank(message = "The alert message cannot be blank.")
        String message,

        Long assignedToId
) {
}
