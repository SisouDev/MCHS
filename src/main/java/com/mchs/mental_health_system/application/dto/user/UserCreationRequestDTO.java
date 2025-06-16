package com.mchs.mental_health_system.application.dto.user;

import com.mchs.mental_health_system.domain.model.enums.userManagement.AccessProfile;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AdministrativeRole;
import com.mchs.mental_health_system.domain.model.enums.userManagement.MedicalSpecialty;
import com.mchs.mental_health_system.domain.model.shared.embeddable.PersonalData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreationRequestDTO(
        @NotBlank String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotNull AccessProfile profile,
        @NotNull @Valid PersonalData personalData,

        MedicalSpecialty specialty,
        String professionalRegistration,
        AdministrativeRole role,
        Long careFacilityId
) {
}
