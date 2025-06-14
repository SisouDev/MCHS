package com.mchs.mental_health_system.domain.model.enums.userManagement;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AccessProfile {
    ROLE_CLINICAL("Clinical Staff"),
    ROLE_ADMINISTRATIVE("Administrative Staff"),
    ROLE_MANAGER("Manager");

    private final String displayName;

    AccessProfile(String displayName) {
        this.displayName = displayName;
    }

    public static AccessProfile fromDisplayName(String text) {
        return Stream.of(AccessProfile.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
