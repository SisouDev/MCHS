package com.mchs.mental_health_system.infrastructure.services.hospitalization;

import com.mchs.mental_health_system.application.dto.hospitalization.AdmissionRequestDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.AdmissionResponseDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.LegalReviewRequestDTO;
import com.mchs.mental_health_system.application.mappers.hospitalization.AdmissionMapper;
import com.mchs.mental_health_system.application.mappers.hospitalization.LegalReviewMapper;
import com.mchs.mental_health_system.application.services.hospitalization.AdmissionService;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.Admission;
import com.mchs.mental_health_system.domain.model.entities.hospitalization.LegalReview;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.shared.functions.Auditable;
import com.mchs.mental_health_system.domain.repositories.hospitalization.AdmissionRepository;
import com.mchs.mental_health_system.domain.repositories.hospitalization.LegalReviewRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.exceptions.patient.PatientNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdmissionServiceImpl implements AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final PatientRepository patientRepository;
    private final AdmissionMapper admissionMapper;
    private final LegalReviewRepository legalReviewRepository;
    private final LegalReviewMapper legalReviewMapper;

    @Override
    @Transactional
    @Auditable(action = "PATIENT_ADMITTED")
    public AdmissionResponseDTO admitPatient(Long patientId, AdmissionRequestDTO requestDTO) {
        Patient patient = findPatientByIdOrThrow(patientId);
        admissionRepository.findActiveByPatientId(patientId).ifPresent(admission -> {
            throw new BusinessException("Patient already has an active admission (ID: " + admission.getId() + ").");
        });
        Admission newAdmission = admissionMapper.toEntity(requestDTO);
        newAdmission.setPatient(patient);
        Admission savedAdmission = admissionRepository.save(newAdmission);
        return admissionMapper.toResponseDTO(savedAdmission);
    }

    @Override
    @Transactional
    @Auditable(action = "PATIENT_DISCHARGED")
    public AdmissionResponseDTO dischargePatient(Long admissionId, LocalDateTime dischargeDate) {
        Admission admission = findAdmissionByIdOrThrow(admissionId);

        if (admission.getDischargeDate() != null) {
            throw new BusinessException("Admission (ID: " + admissionId + ") has already been discharged.");
        }

        if (dischargeDate.isBefore(admission.getAdmissionDate())) {
            throw new BusinessException("Discharge date cannot be before the admission date.");
        }

        admission.setDischargeDate(dischargeDate);
        Admission updatedAdmission = admissionRepository.save(admission);
        return admissionMapper.toResponseDTO(updatedAdmission);
    }

    @Override
    public List<AdmissionResponseDTO> getAdmissionHistoryForPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException(patientId);
        }

        return admissionRepository.findByPatient_IdOrderByAdmissionDateDesc(patientId).stream()
                .map(admissionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Auditable(action = "LEGAL_REVIEW_ADDED")
    public AdmissionResponseDTO addLegalReviewToAdmission(Long admissionId, LegalReviewRequestDTO reviewDTO) {
        Admission admission = findAdmissionByIdOrThrow(admissionId);

        LegalReview newReview = legalReviewMapper.toEntity(reviewDTO);
        newReview.setAdmission(admission);

        admission.getLegalReviews().add(newReview);

        Admission updatedAdmission = admissionRepository.save(admission);
        return admissionMapper.toResponseDTO(updatedAdmission);
    }

    @Override
    @Transactional
    public AdmissionResponseDTO updateLegalReview(Long reviewId, LegalReviewRequestDTO reviewDTO) {
        LegalReview legalReview = findLegalReviewByIdOrThrow(reviewId);

        legalReview.setReviewDate(reviewDTO.reviewDate());
        legalReview.setStatus(reviewDTO.status());
        legalReview.setReviewerNotes(reviewDTO.notes());

        legalReviewRepository.save(legalReview);

        return admissionMapper.toResponseDTO(legalReview.getAdmission());
    }

    @Override
    @Transactional
    public void deleteLegalReview(Long reviewId) {
        LegalReview reviewToDelete = findLegalReviewByIdOrThrow(reviewId);
        legalReviewRepository.delete(reviewToDelete);
    }

    private Admission findAdmissionByIdOrThrow(Long admissionId) {
        return admissionRepository.findById(admissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Admission not found with ID: " + admissionId));
    }

    private Patient findPatientByIdOrThrow(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    private LegalReview findLegalReviewByIdOrThrow(Long reviewId) {
        return legalReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Legal Review not found with ID: " + reviewId));
    }
}
