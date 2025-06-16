package com.mchs.mental_health_system.domain.repositories.user;

import com.mchs.mental_health_system.domain.model.entities.user.AdministrativeProfessional;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AdministrativeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministrativeProfessionalRepository extends JpaRepository<AdministrativeProfessional, Long> {

    List<AdministrativeProfessional> findByRole(AdministrativeRole role);

    List<AdministrativeProfessional> findByCareFacilityId(Long careFacilityId);

    List<AdministrativeProfessional> findByPersonalDataFirstNameContainingIgnoreCaseAndPersonalDataLastNameContainingIgnoreCase(String firstName, String lastName);

}