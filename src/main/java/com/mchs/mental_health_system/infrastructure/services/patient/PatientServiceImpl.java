package com.mchs.mental_health_system.infrastructure.services.patient;

import com.mchs.mental_health_system.application.dto.clinicalRecords.CrisisEventRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.MedicalHistoryEventRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PatientEventResponseDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientCreationDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientResponseDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientSummaryDTO;
import com.mchs.mental_health_system.application.mappers.clinicalRecords.PatientEventMapper;
import com.mchs.mental_health_system.application.mappers.patient.PatientMapper;
import com.mchs.mental_health_system.application.services.patient.PatientService;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.CrisisEvent;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.MedicalHistoryEvent;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.shared.functions.Auditable;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.CrisisEventRepository;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.MedicalHistoryEventRepository;
import com.mchs.mental_health_system.domain.repositories.facility.CareFacilityRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.domain.repositories.user.HealthProfessionalRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.exceptions.patient.PatientNotFoundException;
import com.mchs.mental_health_system.exceptions.user.ProfessionalNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final CareFacilityRepository careFacilityRepository;
    private final CrisisEventRepository crisisEventRepository;
    private final MedicalHistoryEventRepository medicalHistoryEventRepository;
    private final HealthProfessionalRepository healthProfessionalRepository;

    private final PatientMapper patientMapper;
    private final PatientEventMapper patientEventMapper;

    @Override
    @Auditable(action = "VIEW_PATIENT_DETAILS")
    public PatientResponseDTO findById(Long id) {
        Patient patient = findPatientByIdOrThrow(id);
        return patientMapper.toResponseDTO(patient);
    }

    @Override
    @Transactional
    @Auditable(action = "PATIENT_REGISTERED")
    public PatientResponseDTO registerNewPatient(PatientCreationDTO creationDTO) {
        if (patientRepository.existsByPersonalData_Document_Number(creationDTO.personalData().getDocument().getNumber())) {
            throw new BusinessException("A patient with the provided document number already exists.");
        }
        Patient newPatient = patientMapper.toEntity(creationDTO);
        Patient savedPatient = patientRepository.save(newPatient);
        return patientMapper.toResponseDTO(savedPatient);
    }

    @Override
    public List<PatientSummaryDTO> searchByName(String firstName, String lastName) {
        if(!StringUtils.hasText(firstName) && !StringUtils.hasText(lastName)){
            return Collections.emptyList();
        }
        Stream<Patient> patientStream;
        if (StringUtils.hasText(firstName) && StringUtils.hasText(lastName)) {
            patientStream = patientRepository.findByPersonalData_FirstNameContainingIgnoreCaseAndPersonalData_LastNameContainingIgnoreCase(firstName, lastName).stream();
        } else if (StringUtils.hasText(firstName)) {
            patientStream = patientRepository.findByPersonalData_FirstNameContainingIgnoreCase(firstName).stream();
        } else {
            patientStream = patientRepository.findByPersonalData_LastNameContainingIgnoreCase(lastName).stream();
        }

        return patientStream.map(patientMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientSummaryDTO> listByFacility(Long careFacilityId) {
        if (!careFacilityRepository.existsById(careFacilityId)) {
            throw new ResourceNotFoundException("Care Facility not found with ID: " + careFacilityId);
        }
        return patientRepository.findByCareFacilityId(careFacilityId)
                .stream().map(patientMapper::toSummaryDTO)
                .toList();
    }

    @Override
    @Auditable(action = "CRISIS_EVENT_RECORDED")
    public PatientEventResponseDTO recordCrisisEvent(Long patientId, CrisisEventRequestDTO requestDTO) {
        Patient patient = findPatientByIdOrThrow(patientId);
        HealthProfessional reporter = findProfessionalByIdOrThrow(requestDTO.reportedById());
        CrisisEvent event = patientEventMapper.toEntity(requestDTO);
        event.setPatient(patient);
        event.setReportedBy(reporter);
        CrisisEvent savedEvent = crisisEventRepository.save(event);
        return patientEventMapper.toResponseDTO(savedEvent);
    }

    @Override
    public PatientEventResponseDTO recordMedicalHistoryEvent(Long patientId, MedicalHistoryEventRequestDTO requestDTO) {
        Patient patient = findPatientByIdOrThrow(patientId);
        MedicalHistoryEvent event = patientEventMapper.toEntity(requestDTO);
        event.setPatient(patient);
        MedicalHistoryEvent savedEvent = medicalHistoryEventRepository.save(event);
        return patientEventMapper.toResponseDTO(savedEvent);

    }

    private Patient findPatientByIdOrThrow(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    private HealthProfessional findProfessionalByIdOrThrow(Long id) {
        return healthProfessionalRepository.findById(id)
                .orElseThrow(() -> new ProfessionalNotFoundException(id));
    }
}
