package com.mchs.mental_health_system.domain.model.enums.clinicalRecords;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum SeverityLevel {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical");

    private final String displayName;

    SeverityLevel(String displayName) {
        this.displayName = displayName;
    }

    public static SeverityLevel fromDisplayName(String text) {
        return Stream.of(SeverityLevel.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
