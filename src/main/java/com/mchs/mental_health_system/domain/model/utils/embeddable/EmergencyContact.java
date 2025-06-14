package com.mchs.mental_health_system.domain.model.utils.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Embeddable
@Data
public class EmergencyContact {

    @NotBlank(message = "Emergency contact name is required")
    private String fullName;

    @NotBlank(message = "Relationship to the patient is required")
    private String relationship;

    @NotBlank(message = "Emergency contact phone is required")
    private String phone;
}