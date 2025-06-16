package com.mchs.mental_health_system.domain.repositories.user;
import com.mchs.mental_health_system.domain.model.entities.user.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByHealthProfessionalIdAndDayOfWeek(Long professionalId, DayOfWeek dayOfWeek);
}