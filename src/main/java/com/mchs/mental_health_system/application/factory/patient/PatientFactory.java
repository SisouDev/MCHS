package com.mchs.mental_health_system.application.factory.patient;

import com.mchs.mental_health_system.application.dto.patient.PatientCreationDTO;
import com.mchs.mental_health_system.application.mappers.patient.PatientMapper;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientFactory {

    private final PatientMapper patientMapper;

    public Patient create(PatientCreationDTO dto) {
        return patientMapper.toEntity(dto);
    }
}