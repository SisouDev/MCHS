package com.mchs.mental_health_system.application.mappers.hospitalization;

import com.mchs.mental_health_system.application.dto.hospitalization.AlertResponseDTO;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.personalData.fullName", target = "patientFullName")
    @Mapping(source = "assignedTo.id", target = "assignedToProfessionalId")
    @Mapping(source = "assignedTo.personalData.fullName", target = "assignedToProfessionalName")
    AlertResponseDTO toResponseDTO(Alert alert);

}