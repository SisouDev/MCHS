package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class SupervisingProfessionalNotFoundException extends ResourceNotFoundException {
    public SupervisingProfessionalNotFoundException(Long professionalId) {
        super("Supervisory professional not found with ID: " + professionalId);
    }
}