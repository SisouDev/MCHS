package com.mchs.mental_health_system.domain.model.enums.userManagement;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AdministrativeRole {
    RECEPTIONIST("Receptionist"),
    ADMINISTRATIVE_ASSISTANT("Administrative Assistant"),
    CLINIC_MANAGER("Clinic Manager"),
    IT_SUPPORT("IT Support"),
    HUMAN_RESOURCES("Human Resources"),
    FINANCE_OFFICER("Finance Officer"),
    CLEANING_STAFF("Cleaning Staff"),
    SECURITY("Security"),
    OTHER("Other");

    private final String displayName;

    AdministrativeRole(String displayName) {
        this.displayName = displayName;
    }

    public static AdministrativeRole fromDisplayName(String text) {
        return Stream.of(AdministrativeRole.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
