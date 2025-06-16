package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class InvalidAdmissionTypeException extends BusinessException {
    public InvalidAdmissionTypeException(String message) {
        super(message);
    }
}