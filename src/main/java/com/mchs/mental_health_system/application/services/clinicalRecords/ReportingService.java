package com.mchs.mental_health_system.application.services.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisReportDTO;
import com.mchs.mental_health_system.application.dto.facility.FacilityOccupancyReportDTO;

import java.time.LocalDate;

public interface ReportingService {
    DiagnosisReportDTO generateDiagnosisReport(LocalDate start, LocalDate end);

    FacilityOccupancyReportDTO generateFacilityOccupancyReport();
}