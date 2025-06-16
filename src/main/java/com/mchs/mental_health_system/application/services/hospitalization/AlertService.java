package com.mchs.mental_health_system.application.services.hospitalization;

import com.mchs.mental_health_system.application.dto.hospitalization.AlertRequestDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.AlertResponseDTO;

import java.util.List;

public interface AlertService {
    AlertResponseDTO createAlert(Long patientId, AlertRequestDTO requestDTO);
    AlertResponseDTO assignAlert(Long alertId, Long professionalId);
    AlertResponseDTO resolveAlert(Long alertId);
    List<AlertResponseDTO> listOpenAlerts();
}