package com.mchs.mental_health_system.infrastructure.unit.service.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisReportDTO;
import com.mchs.mental_health_system.application.dto.facility.FacilityOccupancyItem;
import com.mchs.mental_health_system.application.dto.facility.FacilityOccupancyReportDTO;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Diagnosis;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.DiagnosisRepository;
import com.mchs.mental_health_system.domain.repositories.facility.CareFacilityRepository;
import com.mchs.mental_health_system.infrastructure.services.clinicalRecords.ReportingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportingServiceImplTest {

    @Mock
    private DiagnosisRepository diagnosisRepository;

    @Mock
    private CareFacilityRepository careFacilityRepository;

    @InjectMocks
    private ReportingServiceImpl reportingService;

    @Test
    @DisplayName("generateDiagnosisReport deve agrupar e contar diagnósticos corretamente")
    void generateDiagnosisReport_shouldGroupAndCountDiagnoses() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        Diagnosis d1 = new Diagnosis();
        d1.setDiagnosisCode("F32.2");
        d1.setDescription("Episódio depressivo grave sem sintomas psicóticos");

        Diagnosis d2 = new Diagnosis();
        d2.setDiagnosisCode("F41.1");
        d2.setDescription("Transtorno de ansiedade generalizada");

        Diagnosis d3 = new Diagnosis();
        d3.setDiagnosisCode("F32.2");
        d3.setDescription("Episódio depressivo grave sem sintomas psicóticos");

        List<Diagnosis> mockDiagnoses = List.of(d1, d2, d3);
        when(diagnosisRepository.findAllByDiagnosisDateBetween(start, end)).thenReturn(mockDiagnoses);

        DiagnosisReportDTO report = reportingService.generateDiagnosisReport(start, end);

        assertThat(report).isNotNull();
        assertThat(report.reportStartDate()).isEqualTo(start);
        assertThat(report.reportEndDate()).isEqualTo(end);
        assertThat(report.totalDiagnoses()).isEqualTo(3L);
        assertThat(report.diagnosesSummary()).hasSize(2);
        assertThat(report.diagnosesSummary()).containsExactlyInAnyOrder(
                new DiagnosisReportDTO.DiagnosisSummaryItem("F32.2", "Episódio depressivo grave sem sintomas psicóticos", 2L),
                new DiagnosisReportDTO.DiagnosisSummaryItem("F41.1", "Transtorno de ansiedade generalizada", 1L)
        );
    }

    @Test
    @DisplayName("generateDiagnosisReport deve retornar relatório vazio quando não há diagnósticos")
    void generateDiagnosisReport_shouldReturnEmptyReport_whenNoDiagnosesFound() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        when(diagnosisRepository.findAllByDiagnosisDateBetween(start, end)).thenReturn(Collections.emptyList());

        DiagnosisReportDTO report = reportingService.generateDiagnosisReport(start, end);

        assertThat(report).isNotNull();
        assertThat(report.totalDiagnoses()).isZero();
        assertThat(report.diagnosesSummary()).isEmpty();
    }

    @Test
    @DisplayName("generateFacilityOccupancyReport deve retornar dados do repositório")
    void generateFacilityOccupancyReport_shouldReturnDataFromRepository() {
        List<FacilityOccupancyItem> mockData = List.of(
                new FacilityOccupancyItem(1L, "Ala Sul", 45, 12),
                new FacilityOccupancyItem(2L, "Ala Norte", 20, 8)
        );

        when(careFacilityRepository.getFacilityOccupancyData()).thenReturn(mockData);

        FacilityOccupancyReportDTO report = reportingService.generateFacilityOccupancyReport();

        assertThat(report).isNotNull();
        assertThat(report.generatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(report.facilitiesData()).hasSize(2);
        assertThat(report.facilitiesData()).isEqualTo(mockData);
    }

    @Test
    @DisplayName("generateFacilityOccupancyReport deve retornar relatório vazio quando não há dados de ocupação")
    void generateFacilityOccupancyReport_shouldReturnEmptyReport_whenNoDataFound() {
        when(careFacilityRepository.getFacilityOccupancyData()).thenReturn(Collections.emptyList());

        FacilityOccupancyReportDTO report = reportingService.generateFacilityOccupancyReport();

        assertThat(report).isNotNull();
        assertThat(report.facilitiesData()).isEmpty();
    }
}