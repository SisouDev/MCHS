package com.mchs.mental_health_system.domain.model.entities.clinicalRecords;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_session_id", nullable = false)
    @ToString.Exclude
    private ConsultationSession consultationSession;

    @Column(nullable = false)
    private String medicationName;

    private String dosage;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(nullable = false)
    private LocalDate prescriptionDate;

    private int durationInDays;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Prescription prescription = (Prescription) o;
        return getId() != null && Objects.equals(getId(), prescription.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}