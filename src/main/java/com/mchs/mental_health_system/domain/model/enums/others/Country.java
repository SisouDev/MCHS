package com.mchs.mental_health_system.domain.model.enums.others;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Country {
    BRAZIL("BR", "Brazil"),
    UNITED_STATES("US", "United States"),
    GERMANY("DE", "Germany"),
    ITALY("IT", "Italy"),
    MEXICO("MX", "Mexico"),
    CHINA("CN", "China"),
    INDIA("IN", "India"),
    RUSSIA("RU", "Russia"),
    SOUTH_AFRICA("ZA", "South Africa"),
    OTHER("OT", "Other");

    private final String code;
    private final String displayName;

    Country(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static Country fromDisplayName(String text) {
        return Stream.of(Country.values())
                .filter(specialty -> specialty.displayName.equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }
}
