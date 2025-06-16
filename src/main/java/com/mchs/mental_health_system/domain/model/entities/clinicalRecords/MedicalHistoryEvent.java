package com.mchs.mental_health_system.domain.model.entities.clinicalRecords;

import com.mchs.mental_health_system.domain.model.enums.patientManagement.ClinicalEventType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "medical_history_events")
@Getter
@Setter
@ToString(callSuper = true)
public class MedicalHistoryEvent extends PatientEvent {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClinicalEventType eventType;
}