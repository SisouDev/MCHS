package com.mchs.mental_health_system.application.controller.clinicalRecords;
import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationResponseDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PrescriptionRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PrescriptionResponseDTO;
import com.mchs.mental_health_system.application.services.clinicalRecords.ConsultationSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/consultation-sessions")
@RequiredArgsConstructor
public class ConsultationSessionController {

    private final ConsultationSessionService sessionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CLINICAL', 'ADMINISTRATIVE')")
    public ResponseEntity<ConsultationResponseDTO> scheduleSession(@Valid @RequestBody ConsultationRequestDTO requestDTO) {
        ConsultationResponseDTO session = sessionService.scheduleSession(requestDTO);
        return ResponseEntity.status(201).body(session);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLINICAL', 'ADMINISTRATIVE')")
    public ResponseEntity<List<ConsultationResponseDTO>> findSessionsByPeriod(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<ConsultationResponseDTO> sessions = sessionService.findSessionsByPeriod(start, end);
        return ResponseEntity.ok(sessions);
    }

    @PatchMapping("/{sessionId}/notes")
    @PreAuthorize("hasRole('CLINICAL')")
    public ResponseEntity<ConsultationResponseDTO> addClinicalNotes(
            @PathVariable Long sessionId,
            @RequestBody String notes) {
        ConsultationResponseDTO session = sessionService.addClinicalNotes(sessionId, notes);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/{sessionId}/prescriptions")
    @PreAuthorize("hasRole('CLINICAL')")
    public ResponseEntity<PrescriptionResponseDTO> addPrescription(
            @PathVariable Long sessionId,
            @Valid @RequestBody PrescriptionRequestDTO requestDTO) {
        PrescriptionResponseDTO prescription = sessionService.addPrescription(sessionId, requestDTO);
        return ResponseEntity.status(201).body(prescription);
    }
}