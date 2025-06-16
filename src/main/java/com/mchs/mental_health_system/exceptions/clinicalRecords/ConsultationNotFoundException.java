package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class ConsultationNotFoundException extends ResourceNotFoundException {
    public ConsultationNotFoundException(Long id) {
        super("Medical appointment session not found with ID: " + id);
    }
}