package com.mchs.mental_health_system.domain.model.enums.hospitalization;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AdmissionType {
    VOLUNTARY("Voluntary"),
    INVOLUNTARY("Involuntary"),
    COMPULSORY("Compulsory");

    private final String displayName;

    AdmissionType(String displayName) {
        this.displayName = displayName;
    }

    public static AdmissionType fromDisplayName(String text) {
        return Stream.of(AdmissionType.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
