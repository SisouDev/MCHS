package com.mchs.mental_health_system.application.dto.clinicalRecords;
import java.time.LocalDate;
import java.util.List;

public record DiagnosisReportDTO(
        LocalDate reportStartDate,
        LocalDate reportEndDate,
        long totalDiagnoses,
        List<DiagnosisSummaryItem> diagnosesSummary
) {
    public record DiagnosisSummaryItem(
            String diagnosisCode,
            String diagnosisDescription,
            long count
    ) {}
}