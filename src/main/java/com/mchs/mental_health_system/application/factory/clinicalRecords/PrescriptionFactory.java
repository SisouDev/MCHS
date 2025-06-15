package com.mchs.mental_health_system.application.factory.clinicalRecords;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PrescriptionRequestDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.ClinicalRecordsMapper;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Prescription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrescriptionFactory {

    private final ClinicalRecordsMapper mapper;

    public Prescription create(PrescriptionRequestDTO dto) {
        return mapper.toEntity(dto);
    }
}