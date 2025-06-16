package com.mchs.mental_health_system.domain.model.entities.user;

import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.ConsultationSession;
import com.mchs.mental_health_system.domain.model.entities.facility.CareFacility;
import com.mchs.mental_health_system.domain.model.enums.userManagement.MedicalSpecialty;
import com.mchs.mental_health_system.domain.model.shared.embeddable.PersonalData;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "health_professionals")
@DiscriminatorValue("HEALTH_PROFESSIONAL")
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

    @OneToMany(mappedBy = "healthProfessional", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ConsultationSession> conductedSessions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_facility_id")
    @ToString.Exclude
    private CareFacility careFacility;

    @OneToMany(mappedBy = "healthProfessional", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Availability> availabilities = new ArrayList<>();

}
