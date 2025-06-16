package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class InvalidLegalReviewStatusException extends BusinessException {
    public InvalidLegalReviewStatusException(String message) {
        super(message);
    }
}