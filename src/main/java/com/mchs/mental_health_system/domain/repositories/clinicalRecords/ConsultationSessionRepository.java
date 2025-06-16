package com.mchs.mental_health_system.domain.repositories.clinicalRecords;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.ConsultationSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationSessionRepository extends JpaRepository<ConsultationSession, Long> {

    List<ConsultationSession> findByPatientId(Long patientId);

    List<ConsultationSession> findByHealthProfessionalId(Long healthProfessionalId);

    List<ConsultationSession> findBySessionDateTimeBetween(LocalDateTime start, LocalDateTime end);


    List<ConsultationSession> findByHealthProfessionalIdAndSessionDateTimeBetween(
            Long professionalId, LocalDateTime start, LocalDateTime end
    );
}