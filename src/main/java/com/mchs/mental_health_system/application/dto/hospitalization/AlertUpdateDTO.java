package com.mchs.mental_health_system.application.dto.hospitalization;

import com.mchs.mental_health_system.domain.model.enums.hospitalization.AlertStatus;
import jakarta.validation.constraints.NotNull;

public record AlertUpdateDTO(
        @NotNull
        AlertStatus status,

        Long assignedToProfessionalId
) {
}
