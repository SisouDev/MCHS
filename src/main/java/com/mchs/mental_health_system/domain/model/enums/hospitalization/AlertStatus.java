package com.mchs.mental_health_system.domain.model.enums.hospitalization;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AlertStatus {
    OPEN("Open"),
    IN_PROGRESS("In progress"),
    RESOLVED("Resolved"),
    DISMISSED("Dismissed");

    private final String displayName;

    AlertStatus(String displayName) {
        this.displayName = displayName;
    }

    public static AlertStatus fromDisplayName(String text) {
        return Stream.of(AlertStatus.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
