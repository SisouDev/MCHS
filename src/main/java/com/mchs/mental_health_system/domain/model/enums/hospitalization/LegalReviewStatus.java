package com.mchs.mental_health_system.domain.model.enums.hospitalization;

import lombok.Getter;

@Getter
public enum LegalReviewStatus {
    SCHEDULED("Scheduled"),
    PENDING_DECISION("Pending Decision"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String displayName;

    LegalReviewStatus(String displayName) {
        this.displayName = displayName;
    }
}