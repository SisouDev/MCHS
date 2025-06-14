package com.mchs.mental_health_system.domain.model.entities.clinicalRecords;

import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.TreatmentPlanStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "treatment_plans")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    @ToString.Exclude
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervising_professional_id")
    @ToString.Exclude
    private HealthProfessional supervisor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TreatmentPlanStatus status;

    @Column(columnDefinition = "TEXT")
    private String objectives;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate expectedEndDate;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TreatmentPlan treatmentPlan = (TreatmentPlan) o;
        return getId() != null && Objects.equals(getId(), treatmentPlan.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}