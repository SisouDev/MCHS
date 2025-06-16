package com.mchs.mental_health_system.domain.repositories.user;

import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.enums.userManagement.MedicalSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthProfessionalRepository extends JpaRepository<HealthProfessional, Long> {

    Optional<HealthProfessional> findByProfessionalRegistration(String professionalRegistration);

    List<HealthProfessional> findBySpecialty(MedicalSpecialty specialty);

    List<HealthProfessional> findByCareFacilityId(Long careFacilityId);

    List<HealthProfessional> findByPersonalDataFirstNameContainingIgnoreCaseAndPersonalDataLastNameContainingIgnoreCase(String firstName, String lastName);

}