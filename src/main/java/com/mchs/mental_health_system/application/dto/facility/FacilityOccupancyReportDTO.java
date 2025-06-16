package com.mchs.mental_health_system.application.dto.facility;

import java.time.LocalDateTime;
import java.util.List;

public record FacilityOccupancyReportDTO(
        LocalDateTime generatedAt,
        List<FacilityOccupancyItem> facilitiesData
) {
}