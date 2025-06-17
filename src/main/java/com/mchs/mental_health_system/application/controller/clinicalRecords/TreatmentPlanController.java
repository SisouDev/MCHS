package com.mchs.mental_health_system.application.controller.clinicalRecords;
import com.mchs.mental_health_system.application.dto.clinicalRecords.TreatmentPlanRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.TreatmentPlanResponseDTO;
import com.mchs.mental_health_system.application.services.clinicalRecords.TreatmentPlanService;
import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.TreatmentPlanStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients/{patientId}/treatment-plan")
@RequiredArgsConstructor
public class TreatmentPlanController {

    private final TreatmentPlanService treatmentPlanService;

    @PostMapping
    @PreAuthorize("hasRole('CLINICAL')")
    public ResponseEntity<TreatmentPlanResponseDTO> createTreatmentPlan(
            @PathVariable Long patientId,
            @Valid @RequestBody TreatmentPlanRequestDTO requestDTO) {
        TreatmentPlanResponseDTO plan = treatmentPlanService.createTreatmentPlan(patientId, requestDTO);
        return ResponseEntity.status(201).body(plan);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLINICAL', 'ADMINISTRATIVE')")
    public ResponseEntity<TreatmentPlanResponseDTO> getTreatmentPlanForPatient(@PathVariable Long patientId) {
        TreatmentPlanResponseDTO plan = treatmentPlanService.getTreatmentPlanForPatient(patientId);
        return ResponseEntity.ok(plan);
    }

    @PatchMapping("/{planId}/status")
    @PreAuthorize("hasRole('CLINICAL')")
    public ResponseEntity<TreatmentPlanResponseDTO> updateTreatmentPlanStatus(
            @PathVariable Long planId,
            @RequestBody TreatmentPlanStatus newStatus) {
        TreatmentPlanResponseDTO plan = treatmentPlanService.updateTreatmentPlanStatus(planId, newStatus);
        return ResponseEntity.ok(plan);
    }
}