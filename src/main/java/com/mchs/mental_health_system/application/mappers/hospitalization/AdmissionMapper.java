package com.mchs.mental_health_system.application.mappers.hospitalization;

import com.mchs.mental_health_system.application.dto.hospitalization.AdmissionRequestDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.AdmissionResponseDTO;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.Admission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {LegalReviewMapper.class})
public interface AdmissionMapper {

    @Mapping(source = "patient.id", target = "patientId")
    AdmissionResponseDTO toResponseDTO(Admission admission);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dischargeDate", ignore = true)
    @Mapping(target = "legalReviews", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Admission toEntity(AdmissionRequestDTO dto);
}