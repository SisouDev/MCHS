package com.mchs.mental_health_system.domain.model.entities.clinicalRecords;

import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.SeverityLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "crisis_events")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CrisisEvent extends PatientEvent {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeverityLevel severity;

    @Column(columnDefinition = "TEXT")
    private String actionTaken;

    @Column(columnDefinition = "TEXT")
    private String outcome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by_id")
    @ToString.Exclude
    private HealthProfessional reportedBy;
}