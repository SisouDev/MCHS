package com.mchs.mental_health_system.domain.model.entities.user;

import com.mchs.mental_health_system.domain.model.entities.facility.CareFacility;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AdministrativeRole;
import com.mchs.mental_health_system.domain.model.shared.embeddable.PersonalData;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "administrative_professionals")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AdministrativeProfessional extends SystemUser {

    @Embedded
    @Valid
    private PersonalData personalData;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdministrativeRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_facility_id")
    @ToString.Exclude
    private CareFacility careFacility;

}