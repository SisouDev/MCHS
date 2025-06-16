package com.mchs.mental_health_system.application.factory.hospitalization;
import com.mchs.mental_health_system.application.dto.hospitalization.AdmissionRequestDTO;
import com.mchs.mental_health_system.application.mappers.hospitalization.AdmissionMapper;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.Admission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdmissionFactory {
    private final AdmissionMapper admissionMapper;
    public Admission create(AdmissionRequestDTO dto) {
        return admissionMapper.toEntity(dto);
    }
}