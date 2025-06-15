package com.mchs.mental_health_system.application.factory.clinicalRecords;
import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisRequestDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.ClinicalRecordsMapper;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Diagnosis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiagnosisFactory {

    private final ClinicalRecordsMapper mapper;

    public Diagnosis create(DiagnosisRequestDTO dto) {
        return mapper.toEntity(dto);
    }
}