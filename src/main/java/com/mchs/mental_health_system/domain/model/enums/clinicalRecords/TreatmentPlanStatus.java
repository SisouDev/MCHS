package com.mchs.mental_health_system.domain.model.enums.clinicalRecords;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum TreatmentPlanStatus {
    ACTIVE("Active"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    UNDER_REVIEW("Under review");

    private final String displayName;

    TreatmentPlanStatus(String displayName) {
        this.displayName = displayName;
    }

    public static TreatmentPlanStatus fromDisplayName(String text) {
        return Stream.of(TreatmentPlanStatus.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
