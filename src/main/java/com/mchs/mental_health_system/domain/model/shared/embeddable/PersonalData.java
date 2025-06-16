package com.mchs.mental_health_system.domain.model.shared.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalData {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @Past
    private LocalDate birthDate;

    @Embedded
    @Valid
    private Document document;

    @NotBlank
    private String phone;


    @Transient
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return "";
        }
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
}