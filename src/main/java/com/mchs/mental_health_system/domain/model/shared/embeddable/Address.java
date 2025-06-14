package com.mchs.mental_health_system.domain.model.shared.embeddable;

import com.mchs.mental_health_system.domain.model.enums.others.Country;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @NotBlank
    @Column(name = "address_street")
    private String street;

    @NotBlank
    @Column(name = "address_city")
    private String city;

    @NotBlank
    @Column(name = "address_state")
    private String state;

    @NotBlank
    @Column(name = "address_postal_code")
    private String postalCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "address_country")
    private Country country;
}