package com.mchs.mental_health_system.domain.model.entities.userManagement;

import com.mchs.mental_health_system.domain.model.enums.userManagement.AdministrativeRole;
import com.mchs.mental_health_system.domain.model.utils.embeddable.PersonalData;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Data;

@Entity
@Table(name = "administrative_professionals")
@Data
public class AdministrativeProfessional extends SystemUser {

    @Embedded
    @Valid
    private PersonalData personalData;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdministrativeRole role;


}