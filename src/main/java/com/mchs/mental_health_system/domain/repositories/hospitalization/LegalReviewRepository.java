package com.mchs.mental_health_system.domain.repositories.hospitalization;

import com.mchs.mental_health_system.domain.model.entities.hospitalization.LegalReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegalReviewRepository extends JpaRepository<LegalReview, Long> {
}
