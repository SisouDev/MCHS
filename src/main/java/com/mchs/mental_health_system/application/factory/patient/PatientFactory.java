package com.mchs.mental_health_system.application.factory.patient;

import com.mchs.mental_health_system.application.dto.patient.PatientRequestDTO;
import com.mchs.mental_health_system.application.mappers.patient.PatientMapper;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientFactory {
    private final PatientMapper patientMapper;

    public Patient create(PatientRequestDTO dto) {
        return patientMapper.toPatientEntity(dto);
    }
}