package com.mchs.mental_health_system.application.factory.hospitalization;

import com.mchs.mental_health_system.domain.model.entities.hospitalization.Alert;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.enums.hospitalization.AlertType;
import org.springframework.stereotype.Component;

@Component
public class AlertFactory {
    public Alert create(Patient patient, AlertType type, String message) {
        Alert alert = new Alert();
        alert.setPatient(patient);
        alert.setType(type);
        alert.setMessage(message);

        return alert;
    }
}