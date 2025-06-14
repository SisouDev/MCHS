package com.mchs.mental_health_system.domain.model.entities.facility;

import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.entities.user.AdministrativeProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.shared.embeddable.Address;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "care_facilities")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CareFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Embedded
    @Valid
    private Address address;

    private String primaryPhone;
    private String primaryEmail;

    @OneToMany(mappedBy = "careFacility", orphanRemoval = true)
    @ToString.Exclude
    private List<Patient> patients = new ArrayList<>();

    @OneToMany(mappedBy = "careFacility", orphanRemoval = true)
    @ToString.Exclude
    private List<HealthProfessional> healthProfessionals = new ArrayList<>();

    @OneToMany(mappedBy = "careFacility", orphanRemoval = true)
    @ToString.Exclude
    private List<AdministrativeProfessional> administrativeProfessionals = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CareFacility careFacility = (CareFacility) o;
        return getId() != null && Objects.equals(getId(), careFacility.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}