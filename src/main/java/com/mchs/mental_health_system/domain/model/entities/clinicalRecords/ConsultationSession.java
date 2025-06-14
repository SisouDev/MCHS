package com.mchs.mental_health_system.domain.model.entities.clinicalRecords;

import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "consultation_sessions")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ConsultationSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @ToString.Exclude
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_professional_id", nullable = false)
    @ToString.Exclude
    private HealthProfessional healthProfessional;

    @Column(nullable = false)
    private LocalDateTime sessionDateTime;

    @Column(columnDefinition = "TEXT")
    private String clinicalNotes;

    @OneToMany(
            mappedBy = "consultationSession",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Prescription> prescriptions = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ConsultationSession consultationSession = (ConsultationSession) o;
        return getId() != null && Objects.equals(getId(), consultationSession.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}