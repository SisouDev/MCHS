package com.mchs.mental_health_system.application.dto.clinicalRecords;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import java.time.LocalDateTime;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CrisisEventResponseDTO.class, name = "CRISIS"),
        @JsonSubTypes.Type(value = MedicalHistoryEventResponseDTO.class, name = "HISTORY")
})
@Data
public abstract class PatientEventResponseDTO {
    private Long id;
    private Long patientId;
    private LocalDateTime eventDateTime;
    private String description;
}