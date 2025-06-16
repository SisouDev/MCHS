package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class ReviewScheduledWithoutAdmissionException extends BusinessException {
    public ReviewScheduledWithoutAdmissionException() {
        super("A legal review should always be associated with an admission.");
    }
}