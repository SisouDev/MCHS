package com.mchs.mental_health_system.application.mappers.patient;

import com.mchs.mental_health_system.application.dto.patient.PatientDetailDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientRequestDTO;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ConsultationMapper.class})
public interface PatientMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "admissions", ignore = true)
    @Mapping(target = "alerts", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "consultationSessions", ignore = true)
    @Mapping(target = "diagnoses", ignore = true)
    @Mapping(target = "careFacility", ignore = true)
    Patient toPatientEntity(PatientRequestDTO dto);

    @Mapping(source = "consultationSessions", target = "recentConsultations")
    PatientDetailDTO toPatientDetailDTO(Patient patient);
}