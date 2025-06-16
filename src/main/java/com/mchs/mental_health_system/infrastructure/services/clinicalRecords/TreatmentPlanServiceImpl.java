package com.mchs.mental_health_system.infrastructure.services.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.TreatmentPlanRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.TreatmentPlanResponseDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.ClinicalRecordsMapper;
import com.mchs.mental_health_system.application.services.clinicalRecords.TreatmentPlanService;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.TreatmentPlan;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.TreatmentPlanStatus;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.TreatmentPlanRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.domain.repositories.user.HealthProfessionalRepository;
import com.mchs.mental_health_system.exceptions.clinicalRecords.DuplicateTreatmentPlanException;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.exceptions.patient.PatientNotFoundException;
import com.mchs.mental_health_system.exceptions.user.ProfessionalNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TreatmentPlanServiceImpl implements TreatmentPlanService {

    private final TreatmentPlanRepository treatmentPlanRepository;
    private final PatientRepository patientRepository;
    private final HealthProfessionalRepository healthProfessionalRepository;
    private final ClinicalRecordsMapper clinicalRecordsMapper;

    @Override
    @Transactional
    public TreatmentPlanResponseDTO createTreatmentPlan(Long patientId, TreatmentPlanRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        treatmentPlanRepository.findByPatientId(patientId).ifPresent(plan -> {
            throw new DuplicateTreatmentPlanException(patientId);
        });

        TreatmentPlan newPlan = clinicalRecordsMapper.toEntity(requestDTO);
        newPlan.setPatient(patient);

        if (requestDTO.supervisorId() != null) {
            HealthProfessional supervisor = healthProfessionalRepository.findById(requestDTO.supervisorId())
                    .orElseThrow(() -> new ProfessionalNotFoundException(requestDTO.supervisorId()));
            newPlan.setSupervisor(supervisor);
        }

        TreatmentPlan savedPlan = treatmentPlanRepository.save(newPlan);
        return clinicalRecordsMapper.toResponseDTO(savedPlan);
    }

    @Override
    @Transactional
    public TreatmentPlanResponseDTO updateTreatmentPlanStatus(Long planId, TreatmentPlanStatus newStatus) {
        TreatmentPlan plan = findPlanByIdOrThrow(planId);
        plan.setStatus(newStatus);

        TreatmentPlan updatedPlan = treatmentPlanRepository.save(plan);
        return clinicalRecordsMapper.toResponseDTO(updatedPlan);
    }

    @Override
    public TreatmentPlanResponseDTO getTreatmentPlanForPatient(Long patientId) {
        TreatmentPlan plan = treatmentPlanRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment Plan not found for patient with ID: " + patientId));

        return clinicalRecordsMapper.toResponseDTO(plan);
    }

    private TreatmentPlan findPlanByIdOrThrow(Long planId) {
        return treatmentPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment Plan not found with ID: " + planId));
    }
}
