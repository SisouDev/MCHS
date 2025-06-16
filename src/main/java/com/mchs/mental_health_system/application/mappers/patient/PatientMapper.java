package com.mchs.mental_health_system.application.mappers.patient;

import com.mchs.mental_health_system.application.dto.patient.PatientCreationDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientResponseDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientSummaryDTO;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ConsultationMapper.class})
public interface PatientMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "careFacility", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "admissions", ignore = true)
    @Mapping(target = "alerts", ignore = true)
    @Mapping(target = "consultationSessions", ignore = true)
    @Mapping(target = "diagnoses", ignore = true)
    Patient toEntity(PatientCreationDTO creationDTO);

    @Mapping(source = "careFacility.id", target = "careFacilityId")
    PatientResponseDTO toResponseDTO(Patient patient);

    @Mapping(source = "personalData.fullName", target = "fullName")
    PatientSummaryDTO toSummaryDTO(Patient patient);
}