package com.mchs.mental_health_system.application.dto.clinicalRecords;
import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.SeverityLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CrisisEventResponseDTO extends PatientEventResponseDTO {
    private SeverityLevel severity;
    private String actionTaken;
    private String outcome;
    private String reportedByName;
}