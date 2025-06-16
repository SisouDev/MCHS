package com.mchs.mental_health_system.infrastructure.services.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisReportDTO;
import com.mchs.mental_health_system.application.dto.facility.FacilityOccupancyItem;
import com.mchs.mental_health_system.application.dto.facility.FacilityOccupancyReportDTO;
import com.mchs.mental_health_system.application.services.clinicalRecords.ReportingService;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Diagnosis;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.DiagnosisRepository;
import com.mchs.mental_health_system.domain.repositories.facility.CareFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {

    private final DiagnosisRepository diagnosisRepository;
    private final CareFacilityRepository careFacilityRepository;

    @Override
    public DiagnosisReportDTO generateDiagnosisReport(LocalDate start, LocalDate end) {
        List<Diagnosis> diagnosesInPeriod = diagnosisRepository.findAllByDiagnosisDateBetween(start, end);

        var summary = diagnosesInPeriod.stream()
                .collect(Collectors.groupingBy(
                        diagnosis -> new DiagnosisReportDTO.DiagnosisSummaryItem(
                                diagnosis.getDiagnosisCode(),
                                diagnosis.getDescription(),
                                0L
                        ),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> new DiagnosisReportDTO.DiagnosisSummaryItem(
                        entry.getKey().diagnosisCode(),
                        entry.getKey().diagnosisDescription(),
                        entry.getValue()
                ))
                .toList();

        return new DiagnosisReportDTO(start, end, diagnosesInPeriod.size(), summary);
    }

    @Override
    public FacilityOccupancyReportDTO generateFacilityOccupancyReport() {
        List<FacilityOccupancyItem> facilitiesData = careFacilityRepository.getFacilityOccupancyData();
        return new FacilityOccupancyReportDTO(LocalDateTime.now(), facilitiesData);
    }
}
