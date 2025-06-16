package com.mchs.mental_health_system.application.controller.patient;

import com.mchs.mental_health_system.application.dto.clinicalRecords.CrisisEventRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.MedicalHistoryEventRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PatientEventResponseDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientCreationDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientResponseDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientSummaryDTO;
import com.mchs.mental_health_system.application.services.patient.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLINICAL', 'ADMINISTRATIVE')")
    public ResponseEntity<PatientResponseDTO> findById(@PathVariable Long id) {
        PatientResponseDTO patient = patientService.findById(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('CLINICAL', 'ADMINISTRATIVE')")
    public ResponseEntity<List<PatientSummaryDTO>> searchByName(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        List<PatientSummaryDTO> patients = patientService.searchByName(firstName, lastName);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/by-facility/{facilityId}")
    @PreAuthorize("hasAnyRole('CLINICAL', 'ADMINISTRATIVE')")
    public ResponseEntity<List<PatientSummaryDTO>> listByFacility(@PathVariable Long facilityId) {
        List<PatientSummaryDTO> patients = patientService.listByFacility(facilityId);
        return ResponseEntity.ok(patients);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATIVE')")
    public ResponseEntity<PatientResponseDTO> registerNewPatient(@Valid @RequestBody PatientCreationDTO creationDTO) {
        PatientResponseDTO newPatient = patientService.registerNewPatient(creationDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newPatient.id()).toUri();
        return ResponseEntity.created(location).body(newPatient);
    }

    @PostMapping("/{patientId}/crisis-events")
    @PreAuthorize("hasRole('CLINICAL')")
    public ResponseEntity<PatientEventResponseDTO> recordCrisisEvent(
            @PathVariable Long patientId,
            @Valid @RequestBody CrisisEventRequestDTO requestDTO) {
        PatientEventResponseDTO event = patientService.recordCrisisEvent(patientId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @PostMapping("/{patientId}/medical-history-events")
    @PreAuthorize("hasRole('CLINICAL')")
    public ResponseEntity<PatientEventResponseDTO> recordMedicalHistoryEvent(
            @PathVariable Long patientId,
            @Valid @RequestBody MedicalHistoryEventRequestDTO requestDTO) {
        PatientEventResponseDTO event = patientService.recordMedicalHistoryEvent(patientId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }
}