package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class LegalReviewNotFoundException extends ResourceNotFoundException {
    public LegalReviewNotFoundException(Long id) {
        super("Legal Review not found with ID: " + id);
    }
}