package com.mchs.mental_health_system.domain.model.enums.userManagement;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum MedicalSpecialty {
    PSYCHIATRIST("Psychiatrist"),
    PSYCHOLOGIST("Psychologist"),
    NURSE("Nurse"),
    SOCIAL_WORKER("Social Worker"),
    OCCUPATIONAL_THERAPIST("Occupational Therapist"),
    PSYCHOMOTOR_THERAPIST("Psychomotor Therapist"),
    GENERAL_PRACTITIONER("General Practitioner"),
    CLINICAL_PSYCHOLOGIST("Clinical Psychologist"),
    OTHER("Other");

    private final String displayName;

    MedicalSpecialty(String displayName) {
        this.displayName = displayName;
    }

    public static MedicalSpecialty fromDisplayName(String text) {
        return Stream.of(MedicalSpecialty.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
