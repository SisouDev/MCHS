package com.mchs.mental_health_system.infrastructure.unit.service.patient;

import com.mchs.mental_health_system.application.dto.patient.PatientCreationDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientSummaryDTO;
import com.mchs.mental_health_system.application.mappers.patient.PatientMapper;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.enums.others.Country;
import com.mchs.mental_health_system.domain.model.enums.others.DocumentType;
import com.mchs.mental_health_system.domain.model.shared.embeddable.Document;
import com.mchs.mental_health_system.domain.model.shared.embeddable.EmergencyContact;
import com.mchs.mental_health_system.domain.model.shared.embeddable.PersonalData;
import com.mchs.mental_health_system.domain.repositories.facility.CareFacilityRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.exceptions.common.BusinessException;
import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;
import com.mchs.mental_health_system.infrastructure.services.patient.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private CareFacilityRepository careFacilityRepository;
    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientCreationDTO creationDTO;
    private PersonalData personalData;
    private Document document;

    @BeforeEach
    void setUp() {
        document = new Document(DocumentType.NATIONAL_ID, "123.456.789-00", Country.BRAZIL);

        personalData = new PersonalData(
                "João",
                "Silva",
                LocalDate.of(1990, 5, 15),
                document,
                "11987654321"
        );

        EmergencyContact emergencyContact = new EmergencyContact();

        emergencyContact.setFullName("Maria Silva");
        emergencyContact.setRelationship("Mãe");
        emergencyContact.setPhone("11987654322");

        patient = new Patient();
        patient.setId(1L);
        patient.setPersonalData(personalData);
        patient.setEmergencyContact(emergencyContact);

        creationDTO = new PatientCreationDTO(personalData, emergencyContact, 1L);
    }

    @Test
    @DisplayName("registerNewPatient deve registrar paciente com sucesso")
    void registerNewPatient_shouldRegisterSuccessfully() {
        when(patientRepository.existsByPersonalData_Document_Number("123.456.789-00")).thenReturn(false);
        when(patientMapper.toEntity(creationDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);

        patientService.registerNewPatient(creationDTO);

        verify(patientRepository).save(patient);
    }

    @Test
    @DisplayName("registerNewPatient deve lançar exceção se documento já existir")
    void registerNewPatient_shouldThrowException_whenDocumentExists() {
        when(patientRepository.existsByPersonalData_Document_Number("123.456.789-00")).thenReturn(true);

        assertThatThrownBy(() -> patientService.registerNewPatient(creationDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("A patient with the provided document number already exists.");
    }

    @Test
    @DisplayName("findById deve retornar DTO do paciente")
    void findById_shouldReturnPatientDto() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        patientService.findById(1L);

        verify(patientMapper).toResponseDTO(patient);
    }

    @Test
    @DisplayName("searchByName deve buscar por nome e sobrenome")
    void searchByName_shouldSearchByFirstAndLastName() {
        when(patientRepository.findByPersonalData_FirstNameContainingIgnoreCaseAndPersonalData_LastNameContainingIgnoreCase("João", "Silva"))
                .thenReturn(List.of(patient));
        when(patientMapper.toSummaryDTO(patient)).thenReturn(new PatientSummaryDTO(1L, "João Silva", "123.456.789-00"));

        List<PatientSummaryDTO> results = patientService.searchByName("João", "Silva");

        assertThat(results).hasSize(1);
        verify(patientRepository).findByPersonalData_FirstNameContainingIgnoreCaseAndPersonalData_LastNameContainingIgnoreCase("João", "Silva");
    }

    @Test
    @DisplayName("listByFacility deve retornar pacientes da unidade")
    void listByFacility_shouldReturnPatientsFromFacility() {
        when(careFacilityRepository.existsById(1L)).thenReturn(true);
        when(patientRepository.findByCareFacilityId(1L)).thenReturn(List.of(patient));

        patientService.listByFacility(1L);

        verify(patientMapper).toSummaryDTO(patient);
    }

    @Test
    @DisplayName("listByFacility deve lançar exceção se a unidade não existir")
    void listByFacility_shouldThrowException_whenFacilityNotFound() {
        when(careFacilityRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> patientService.listByFacility(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}