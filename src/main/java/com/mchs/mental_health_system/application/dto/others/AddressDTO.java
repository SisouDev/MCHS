package com.mchs.mental_health_system.application.dto.others;

public record AddressDTO(
        String street,
        String city,
        String state,
        String zipCode
) {}