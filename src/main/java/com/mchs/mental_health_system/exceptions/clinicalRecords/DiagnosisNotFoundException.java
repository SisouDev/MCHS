package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class DiagnosisNotFoundException extends ResourceNotFoundException {
    public DiagnosisNotFoundException(Long id) {
        super("Diagnostic not found with ID: " + id);
    }
}