package com.mchs.mental_health_system.application.dto.hospitalization;

import com.mchs.mental_health_system.domain.model.enums.hospitalization.AdmissionType;

import java.time.LocalDateTime;
import java.util.List;

public record AdmissionResponseDTO(
        Long id,
        Long patientId,
        AdmissionType type,
        LocalDateTime admissionDate,
        LocalDateTime dischargeDate,
        String reason,
        String ward,
        List<LegalReviewResponseDTO> legalReviews
) {
}
