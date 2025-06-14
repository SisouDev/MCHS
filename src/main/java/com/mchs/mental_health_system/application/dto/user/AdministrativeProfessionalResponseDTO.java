package com.mchs.mental_health_system.application.dto.user;

import com.mchs.mental_health_system.domain.model.enums.userManagement.AdministrativeRole;

import java.time.LocalDate;

public record AdministrativeProfessionalResponseDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        LocalDate birthDate,
        AdministrativeRole role
) {
}
