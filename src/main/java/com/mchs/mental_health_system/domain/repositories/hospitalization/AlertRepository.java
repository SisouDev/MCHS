package com.mchs.mental_health_system.domain.repositories.hospitalization;

import com.mchs.mental_health_system.domain.model.entities.hospitalization.Alert;
import com.mchs.mental_health_system.domain.model.enums.hospitalization.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByStatus(AlertStatus status);
    List<Alert> findByPatientId(Long patientId);
    List<Alert> findByAssignedToId(Long healthProfessionalId);
}