package com.mchs.mental_health_system.application.factory.clinicalRecords;
import com.mchs.mental_health_system.application.dto.clinicalRecords.ConsultationRequestDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.ClinicalRecordsMapper;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.ConsultationSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsultationSessionFactory {
    private final ClinicalRecordsMapper mapper;

    public ConsultationSession create(ConsultationRequestDTO dto) {
        return mapper.toEntity(dto);
    }
}