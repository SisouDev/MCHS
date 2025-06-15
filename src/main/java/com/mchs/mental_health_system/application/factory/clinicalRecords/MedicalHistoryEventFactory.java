package com.mchs.mental_health_system.application.factory.clinicalRecords;
import com.mchs.mental_health_system.application.dto.clinicalRecords.MedicalHistoryEventRequestDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.PatientEventMapper;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.MedicalHistoryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicalHistoryEventFactory {

    private final PatientEventMapper patientEventMapper;

    public MedicalHistoryEvent create(MedicalHistoryEventRequestDTO dto) {
        return patientEventMapper.toEntity(dto);
    }
}