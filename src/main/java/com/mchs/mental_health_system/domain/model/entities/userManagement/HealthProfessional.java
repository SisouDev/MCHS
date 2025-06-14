package com.mchs.mental_health_system.domain.model.entities.userManagement;

import com.mchs.mental_health_system.domain.model.enums.userManagement.MedicalSpecialty;
import com.mchs.mental_health_system.domain.model.utils.embeddable.PersonalData;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "health_professionals")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class HealthProfessional extends SystemUser {

    @Embedded
    @Valid
    private PersonalData personalData;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicalSpecialty specialty;

    @Column(nullable = false, unique = true)
    private String professionalRegistration;

}
