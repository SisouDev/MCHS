package com.mchs.mental_health_system.application.controller.hospitalization;

import com.mchs.mental_health_system.application.dto.hospitalization.AlertRequestDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.AlertResponseDTO;
import com.mchs.mental_health_system.application.services.hospitalization.AlertService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping("/for-patient/{patientId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AlertResponseDTO> createAlert(
            @PathVariable Long patientId,
            @Valid @RequestBody AlertRequestDTO requestDTO) {
        AlertResponseDTO alert = alertService.createAlert(patientId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(alert);
    }

    @GetMapping("/open")
    @PreAuthorize("hasAnyRole('CLINICAL', 'ADMINISTRATIVE')")
    public ResponseEntity<List<AlertResponseDTO>> listOpenAlerts() {
        List<AlertResponseDTO> alerts = alertService.listOpenAlerts();
        return ResponseEntity.ok(alerts);
    }

    public record AssignAlertRequestDTO(@NotNull Long professionalId){}

    @PatchMapping("/{alertId}/assign")
    @PreAuthorize("hasAnyRole('CLINICAL', 'MANAGER')")
    public ResponseEntity<AlertResponseDTO> assignAlert(
            @PathVariable Long alertId,
            @Valid @RequestBody AssignAlertRequestDTO requestDTO) {
        AlertResponseDTO alert = alertService.assignAlert(alertId, requestDTO.professionalId());
        return ResponseEntity.ok(alert);
    }

    @PatchMapping("/{alertId}/resolve")
    @PreAuthorize("hasRole('CLINICAL')")
    public ResponseEntity<AlertResponseDTO> resolveAlert(@PathVariable Long alertId) {
        AlertResponseDTO alert = alertService.resolveAlert(alertId);
        return ResponseEntity.ok(alert);
    }
}