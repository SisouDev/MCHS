package com.mchs.mental_health_system.application.mappers.facility;

import com.mchs.mental_health_system.application.dto.facility.CareFacilityRequestDTO;
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
    CareFacility toEntity(CareFacilityRequestDTO dto);

    @Mapping(target = "patientCount", expression = "java(facility.getPatients() != null ? facility.getPatients().size() : 0)")
    @Mapping(target = "healthProfessionalCount", expression = "java(facility.getHealthProfessionals() != null ? facility.getHealthProfessionals().size() : 0)")
    @Mapping(target = "administrativeProfessionalCount", expression = "java(facility.getAdministrativeProfessionals() != null ? facility.getAdministrativeProfessionals().size() : 0)")
    CareFacilityResponseDTO toResponseDTO(CareFacility facility);
}