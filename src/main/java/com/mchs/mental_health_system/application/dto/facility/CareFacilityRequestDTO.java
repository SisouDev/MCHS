package com.mchs.mental_health_system.application.dto.facility;

import com.mchs.mental_health_system.domain.model.shared.embeddable.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CareFacilityRequestDTO(
        @NotBlank String name,
        @NotNull @Valid Address address,
        @NotBlank String primaryPhone,
        @Email String primaryEmail
) {
}
