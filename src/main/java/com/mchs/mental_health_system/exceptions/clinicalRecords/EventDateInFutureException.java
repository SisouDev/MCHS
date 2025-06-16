package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

import java.time.LocalDateTime;

public class EventDateInFutureException extends BusinessException {
    public EventDateInFutureException(LocalDateTime eventDate) {
        super("The date of the event (" + eventDate + ") cannot be in the future.");
    }
}