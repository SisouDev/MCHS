package com.mchs.mental_health_system.domain.model.enums.patientManagement;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum ClinicalEventType {
    DIAGNOSIS_RECORD("Diagnosis record"),
    SURGERY("Surgery"),
    ALLERGY_RECORD("Allergy record"),
    CHRONIC_DISEASE_RECORD("Chronic disease record"),
    PREVIOUS_TREATMENT("Previous treatment"),
    HOSPITALIZATION_RECORD("Hospitalization record");

    private final String displayName;

    ClinicalEventType(String displayName) {
        this.displayName = displayName;
    }

    public static ClinicalEventType fromDisplayName(String text) {
        return Stream.of(ClinicalEventType.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
