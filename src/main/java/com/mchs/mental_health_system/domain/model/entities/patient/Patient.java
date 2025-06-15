package com.mchs.mental_health_system.domain.model.entities.patient;

import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.ConsultationSession;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Diagnosis;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.PatientEvent;
import com.mchs.mental_health_system.domain.model.entities.facility.CareFacility;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.Admission;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.Alert;
import com.mchs.mental_health_system.domain.model.shared.embeddable.EmergencyContact;
import com.mchs.mental_health_system.domain.model.shared.embeddable.PersonalData;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "patients")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Valid
    private PersonalData personalData;

    @Embedded
    @Valid
    @AttributeOverrides({
            @AttributeOverride(name = "fullName", column = @Column(name = "emergency_contact_full_name")),
            @AttributeOverride(name = "relationship", column = @Column(name = "emergency_contact_relationship")),
            @AttributeOverride(name = "phone", column = @Column(name = "emergency_contact_phone"))
    })
    private EmergencyContact emergencyContact;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Admission> admissions = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Alert> alerts = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PatientEvent> events = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ConsultationSession> consultationSessions = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "care_facility_id")
    @ToString.Exclude
    private CareFacility careFacility;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Patient patient = (Patient) o;
        return getId() != null && Objects.equals(getId(), patient.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
