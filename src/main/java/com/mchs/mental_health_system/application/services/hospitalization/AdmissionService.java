package com.mchs.mental_health_system.application.services.hospitalization;

import com.mchs.mental_health_system.application.dto.hospitalization.AdmissionRequestDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.AdmissionResponseDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.LegalReviewRequestDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface AdmissionService {
    AdmissionResponseDTO admitPatient(Long patientId, AdmissionRequestDTO requestDTO);

    AdmissionResponseDTO dischargePatient(Long admissionId, LocalDateTime dischargeDate);

    List<AdmissionResponseDTO> getAdmissionHistoryForPatient(Long patientId);

    AdmissionResponseDTO addLegalReviewToAdmission(Long admissionId, LegalReviewRequestDTO reviewDTO);

    AdmissionResponseDTO updateLegalReview(Long reviewId, LegalReviewRequestDTO reviewDTO);

    void deleteLegalReview(Long reviewId);
}