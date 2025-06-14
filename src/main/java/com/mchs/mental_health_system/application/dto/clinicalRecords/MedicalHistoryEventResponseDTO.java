package com.mchs.mental_health_system.application.dto.clinicalRecords;
import com.mchs.mental_health_system.domain.model.enums.patientManagement.ClinicalEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MedicalHistoryEventResponseDTO extends PatientEventResponseDTO {
    private ClinicalEventType eventType;
}