package com.mchs.mental_health_system.domain.model.enums.hospitalization;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AlertType {
    CRITICAL_MISSED_APPOINTMENT("Critical missed appointment"),
    NO_CONTACT("No contact"),
    BEHAVIORAL_CHANGE("Behavioral change"),
    MEDICATION_NON_ADHERENCE("Medication non adherence"),
    UPCOMING_LEGAL_REVIEW("Upcoming legal review");

    private final String displayName;

    AlertType(String displayName) {
        this.displayName = displayName;
    }

    public static AlertType fromDisplayName(String text) {
        return Stream.of(AlertType.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
