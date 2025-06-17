package com.mchs.mental_health_system.application.mappers.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.*;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.CrisisEvent;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.MedicalHistoryEvent;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.PatientEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientEventMapper {

    default PatientEventResponseDTO toResponseDTO(PatientEvent event) {
        if (event instanceof CrisisEvent) {
            return toResponseDTO((CrisisEvent) event);
        } else if (event instanceof MedicalHistoryEvent) {
            return toResponseDTO((MedicalHistoryEvent) event);
        }
        throw new IllegalArgumentException("Unknown PatientEvent subtype: " + event.getClass().getName());
    }

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "reportedBy.personalData.fullName", target = "reportedByName")
    CrisisEventResponseDTO toResponseDTO(CrisisEvent event);

    @Mapping(source = "patient.id", target = "patientId")
    MedicalHistoryEventResponseDTO toResponseDTO(MedicalHistoryEvent event);


    List<PatientEventResponseDTO> toResponseDTOList(List<PatientEvent> events);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "reportedBy", ignore = true)
    CrisisEvent toEntity(CrisisEventRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    MedicalHistoryEvent toEntity(MedicalHistoryEventRequestDTO dto);
}