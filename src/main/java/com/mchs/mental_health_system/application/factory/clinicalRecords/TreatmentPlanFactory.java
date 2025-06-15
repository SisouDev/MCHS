package com.mchs.mental_health_system.application.factory.clinicalRecords;
import com.mchs.mental_health_system.application.dto.clinicalRecords.TreatmentPlanRequestDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.ClinicalRecordsMapper;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.TreatmentPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TreatmentPlanFactory {

    private final ClinicalRecordsMapper mapper;

    public TreatmentPlan create(TreatmentPlanRequestDTO dto) {
        return mapper.toEntity(dto);
    }
}