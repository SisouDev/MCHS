package com.mchs.mental_health_system.application.dto.user;

import com.mchs.mental_health_system.domain.model.enums.userManagement.MedicalSpecialty;

import java.time.LocalDate;

public record HealthProfessionalResponseDTO(
        Long id,
        String username,
        String firstName,
        String lastName,
        LocalDate birthDate,
        MedicalSpecialty specialty,
        String professionalRegistration
) {
}
