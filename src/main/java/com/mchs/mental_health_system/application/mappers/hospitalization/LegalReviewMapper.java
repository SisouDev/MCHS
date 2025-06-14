package com.mchs.mental_health_system.application.mappers.hospitalization;

import com.mchs.mental_health_system.application.dto.hospitalization.LegalReviewRequestDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.LegalReviewResponseDTO;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.LegalReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LegalReviewMapper {

    @Mapping(source = "admission.id", target = "admissionId")
    LegalReviewResponseDTO toResponseDTO(LegalReview legalReview);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "admission", ignore = true)
    LegalReview toEntity(LegalReviewRequestDTO dto);
}