package com.mchs.mental_health_system.application.dto.clinicalRecords;

import java.time.LocalDateTime;
import java.util.List;

public record ConsultationResponseDTO(
        Long id,
        LocalDateTime sessionDateTime,
        String clinicalNotes,
        Long patientId,
        String patientName,
        Long healthProfessionalId,
        String healthProfessionalName,
        List<PrescriptionResponseDTO> prescriptions
) {
}
