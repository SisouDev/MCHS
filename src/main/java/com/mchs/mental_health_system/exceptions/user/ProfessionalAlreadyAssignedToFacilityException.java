package com.mchs.mental_health_system.exceptions.user;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class ProfessionalAlreadyAssignedToFacilityException extends BusinessException {
    public ProfessionalAlreadyAssignedToFacilityException(Long professionalId, Long facilityId) {
        super("The professional with ID " + professionalId + " is already associated with the health unit with ID " + facilityId);
    }
}