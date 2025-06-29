package com.mchs.mental_health_system.application.mappers.user;

import com.mchs.mental_health_system.application.dto.user.AdministrativeProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.HealthProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.UserCreationRequestDTO;
import com.mchs.mental_health_system.domain.model.entities.user.AdministrativeProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "lastAccess", ignore = true)
    @Mapping(target = "careFacility", ignore = true)
    @Mapping(source = "careFacilityId", target = "careFacility.id")
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "conductedSessions", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    HealthProfessional toHealthProfessional(UserCreationRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(source = "careFacilityId", target = "careFacility.id")
    @Mapping(target = "lastAccess", ignore = true)
    @Mapping(target = "careFacility", ignore = true)
    @Mapping(target = "authorities", ignore = true) //
    AdministrativeProfessional toAdministrativeProfessional(UserCreationRequestDTO dto);


    @Mapping(source = "personalData.firstName", target = "firstName")
    @Mapping(source = "personalData.lastName", target = "lastName")
    @Mapping(source = "personalData.birthDate", target = "birthDate")
    HealthProfessionalResponseDTO toHealthProfessionalResponseDTO(HealthProfessional professional);

    @Mapping(source = "personalData.firstName", target = "firstName")
    @Mapping(source = "personalData.lastName", target = "lastName")
    @Mapping(source = "personalData.birthDate", target = "birthDate")
    AdministrativeProfessionalResponseDTO toAdministrativeProfessionalResponseDTO(AdministrativeProfessional professional);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "lastAccess", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Manager toManager(UserCreationRequestDTO dto);
}