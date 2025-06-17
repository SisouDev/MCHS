package com.mchs.mental_health_system.application.controller.hospitalization;

import com.mchs.mental_health_system.application.dto.hospitalization.AdmissionRequestDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.AdmissionResponseDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.LegalReviewRequestDTO;
import com.mchs.mental_health_system.application.services.hospitalization.AdmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AdmissionController {

    private final AdmissionService admissionService;

    @PostMapping("/patients/{patientId}/admissions")
    @PreAuthorize("hasAnyRole('CLINICAL', 'ADMINISTRATIVE')")
    public ResponseEntity<AdmissionResponseDTO> admitPatient(
            @PathVariable Long patientId,
            @Valid @RequestBody AdmissionRequestDTO requestDTO) {
        AdmissionResponseDTO admission = admissionService.admitPatient(patientId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(admission);
    }

    @GetMapping("/patients/{patientId}/admissions")
    @PreAuthorize("hasAnyRole('CLINICAL', 'ADMINISTRATIVE')")
    public ResponseEntity<List<AdmissionResponseDTO>> getAdmissionHistoryForPatient(@PathVariable Long patientId) {
        List<AdmissionResponseDTO> history = admissionService.getAdmissionHistoryForPatient(patientId);
        return ResponseEntity.ok(history);
    }

    public record DischargeRequestDTO(LocalDateTime dischargeDate) {}

    @PatchMapping("/admissions/{admissionId}/discharge")
    @PreAuthorize("hasRole('CLINICAL')")
    public ResponseEntity<AdmissionResponseDTO> dischargePatient(
            @PathVariable Long admissionId,
            @RequestBody DischargeRequestDTO requestDTO) {
        AdmissionResponseDTO admission = admissionService.dischargePatient(admissionId, requestDTO.dischargeDate());
        return ResponseEntity.ok(admission);
    }

    @PostMapping("/admissions/{admissionId}/legal-reviews")
    @PreAuthorize("hasAnyRole('ADMINISTRATIVE', 'MANAGER')")
    public ResponseEntity<AdmissionResponseDTO> addLegalReview(
            @PathVariable Long admissionId,
            @Valid @RequestBody LegalReviewRequestDTO requestDTO) {
        AdmissionResponseDTO admission = admissionService.addLegalReviewToAdmission(admissionId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(admission);
    }
}