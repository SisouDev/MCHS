package com.mchs.mental_health_system.application.mappers.patient;

import com.mchs.mental_health_system.application.dto.patient.ConsultationSummaryDTO;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.ConsultationSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    @Mapping(source = "healthProfessional.personalData.fullName", target = "healthProfessionalName")
    ConsultationSummaryDTO toSummaryDTO(ConsultationSession session);
}