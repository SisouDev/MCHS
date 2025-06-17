package com.mchs.mental_health_system.application.services.user;

import com.mchs.mental_health_system.application.dto.user.AdministrativeProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.HealthProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.UserCreationRequestDTO;
import com.mchs.mental_health_system.domain.model.entities.user.SystemUser;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AdministrativeRole;
import com.mchs.mental_health_system.domain.model.enums.userManagement.MedicalSpecialty;

import java.util.List;

public interface UserService {

    SystemUser createUser(UserCreationRequestDTO creationDTO);

    List<HealthProfessionalResponseDTO> findHealthProfessionalsBySpecialty(MedicalSpecialty specialty);

    List<AdministrativeProfessionalResponseDTO> findAdministrativeProfessionalsByRole(AdministrativeRole role);

}