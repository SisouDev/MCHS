package com.mchs.mental_health_system.application.mappers.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.CrisisEventRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.CrisisEventResponseDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.MedicalHistoryEventResponseDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PatientEventResponseDTO;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.CrisisEvent;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.MedicalHistoryEvent;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.PatientEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface PatientEventMapper {

    @SubclassMapping(source = CrisisEvent.class, target = CrisisEventResponseDTO.class)
    @SubclassMapping(source = MedicalHistoryEvent.class, target = MedicalHistoryEventResponseDTO.class)
    @Mapping(source = "patient.id", target = "patientId")
    PatientEventResponseDTO toResponseDTO(PatientEvent event);

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "reportedBy.personalData.fullName", target = "reportedByName")
    CrisisEventResponseDTO toResponseDTO(CrisisEvent crisisEvent);

    @Mapping(source = "patient.id", target = "patientId")
    MedicalHistoryEventResponseDTO toResponseDTO(MedicalHistoryEvent medicalHistoryEvent);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "reportedBy", ignore = true)
    CrisisEvent toEntity(CrisisEventRequestDTO dto);

}