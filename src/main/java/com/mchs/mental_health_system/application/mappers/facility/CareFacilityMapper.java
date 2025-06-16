package com.mchs.mental_health_system.application.mappers.facility;

import com.mchs.mental_health_system.application.dto.facility.CareFacilityCreationDTO;
import com.mchs.mental_health_system.application.dto.facility.CareFacilityResponseDTO;
import com.mchs.mental_health_system.domain.model.entities.facility.CareFacility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CareFacilityMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patients", ignore = true)
    @Mapping(target = "healthProfessionals", ignore = true)
    @Mapping(target = "administrativeProfessionals", ignore = true)
    CareFacility toEntity(CareFacilityCreationDTO dto);
    @Mapping(target = "patientCount", expression = "java(facility.getPatients().size())")
    @Mapping(target = "healthProfessionalCount", expression = "java(facility.getHealthProfessionals().size())")
    @Mapping(target = "administrativeProfessionalCount", expression = "java(facility.getAdministrativeProfessionals().size())")
    CareFacilityResponseDTO toResponseDTO(CareFacility facility);
}