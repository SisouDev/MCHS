package com.mchs.mental_health_system.application.factory.clinicalRecords;
import com.mchs.mental_health_system.application.dto.clinicalRecords.CrisisEventRequestDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.PatientEventMapper;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.CrisisEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrisisEventFactory {
    private final PatientEventMapper patientEventMapper;

    public CrisisEvent create(CrisisEventRequestDTO dto) {
        return patientEventMapper.toEntity(dto);
    }
}