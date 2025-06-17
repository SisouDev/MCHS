package com.mchs.mental_health_system.application.controller.clinicalRecords;
import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisResponseDTO;
import com.mchs.mental_health_system.application.services.clinicalRecords.DiagnosisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients/{patientId}/diagnoses")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @PostMapping
    @PreAuthorize("hasRole('CLINICAL')")
    public ResponseEntity<DiagnosisResponseDTO> addDiagnosis(
            @PathVariable Long patientId,
            @Valid @RequestBody DiagnosisRequestDTO requestDTO) {
        DiagnosisResponseDTO diagnosis = diagnosisService.addDiagnosis(patientId, requestDTO);
        return ResponseEntity.status(201).body(diagnosis);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLINICAL', 'ADMINISTRATIVE')")
    public ResponseEntity<List<DiagnosisResponseDTO>> getDiagnosesForPatient(@PathVariable Long patientId) {
        List<DiagnosisResponseDTO> diagnoses = diagnosisService.getDiagnosesForPatient(patientId);
        return ResponseEntity.ok(diagnoses);
    }

    @PutMapping("/{diagnosisId}/set-primary")
    @PreAuthorize("hasRole('CLINICAL')")
    public ResponseEntity<DiagnosisResponseDTO> setAsPrimaryDiagnosis(@PathVariable Long diagnosisId) {
        DiagnosisResponseDTO diagnosis = diagnosisService.setAsPrimaryDiagnosis(diagnosisId);
        return ResponseEntity.ok(diagnosis);
    }
}