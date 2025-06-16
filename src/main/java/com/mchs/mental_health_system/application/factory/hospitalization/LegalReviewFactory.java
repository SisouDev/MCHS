package com.mchs.mental_health_system.application.factory.hospitalization;
import com.mchs.mental_health_system.application.dto.hospitalization.LegalReviewRequestDTO;
import com.mchs.mental_health_system.application.mappers.hospitalization.LegalReviewMapper;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.LegalReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LegalReviewFactory {
    private final LegalReviewMapper legalReviewMapper;
    public LegalReview create(LegalReviewRequestDTO dto) {
        return legalReviewMapper.toEntity(dto);
    }
}