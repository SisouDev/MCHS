package com.mchs.mental_health_system.application.mappers.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.*;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.ConsultationSession;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Diagnosis;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.Prescription;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.TreatmentPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClinicalRecordsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "consultationSession", ignore = true)
    Prescription toEntity(PrescriptionRequestDTO dto);

    @Mapping(source = "consultationSession.id", target = "consultationSessionId")
    PrescriptionResponseDTO toResponseDTO(Prescription entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "supervisor", ignore = true)
    TreatmentPlan toEntity(TreatmentPlanRequestDTO dto);

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.personalData.fullName", target = "patientName")
    @Mapping(source = "supervisor.id", target = "supervisorId")
    @Mapping(source = "supervisor.personalData.fullName", target = "supervisorName")
    TreatmentPlanResponseDTO toResponseDTO(TreatmentPlan entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Diagnosis toEntity(DiagnosisRequestDTO dto);
    @Mapping(source = "patient.id", target = "patientId")
    DiagnosisResponseDTO toResponseDTO(Diagnosis entity);
    List<DiagnosisResponseDTO> toDiagnosisResponseDTOList(List<Diagnosis> diagnoses);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "healthProfessional", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    ConsultationSession toEntity(ConsultationRequestDTO dto);

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.personalData.fullName", target = "patientName")
    @Mapping(source = "healthProfessional.personalData.fullName", target = "healthProfessionalName")
    @Mapping(source = "healthProfessional.id", target = "healthProfessionalId")
    ConsultationResponseDTO toResponseDTO(ConsultationSession entity);
}