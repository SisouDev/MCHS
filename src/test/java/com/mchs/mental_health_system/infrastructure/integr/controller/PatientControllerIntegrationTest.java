package com.mchs.mental_health_system.infrastructure.integr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchs.mental_health_system.application.dto.patient.PatientCreationDTO;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.enums.others.Country;
import com.mchs.mental_health_system.domain.model.enums.others.DocumentType;
import com.mchs.mental_health_system.domain.model.shared.embeddable.Document;
import com.mchs.mental_health_system.domain.model.shared.embeddable.EmergencyContact;
import com.mchs.mental_health_system.domain.model.shared.embeddable.PersonalData;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    @DisplayName("POST /api/v1/patients - Deve registrar um novo paciente com sucesso")
    @WithMockUser(roles = "ADMINISTRATIVE")
    void registerNewPatient_shouldReturn201_whenDataIsValid() throws Exception {
        // Arrange
        Document document = new Document(DocumentType.NATIONAL_ID, "111.222.333-44", Country.BRAZIL);
        PersonalData personalData = new PersonalData("Mariana", "Costa", LocalDate.of(1992, 10, 25), document, "32912345678");
        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setPhone("32987654321");
        emergencyContact.setFullName("José Costa");
        emergencyContact.setRelationship("Pai");
        PatientCreationDTO creationDTO = new PatientCreationDTO(personalData, emergencyContact, null);

        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.personalData.firstName").value("Mariana"))
                .andExpect(jsonPath("$.emergencyContact.relationship").value("Pai"));

        Optional<Patient> savedPatientOpt = patientRepository.findByPersonalDataDocumentNumber("111.222.333-44");
        assertThat(savedPatientOpt).isPresent();
        assertThat(savedPatientOpt.get().getPersonalData().getLastName()).isEqualTo("Costa");
    }

    @Test
    @DisplayName("POST /api/v1/patients - Deve retornar 403 Forbidden se o usuário não tiver o papel correto")
    @WithMockUser(roles = "CLINICAL")
    void registerNewPatient_shouldReturn403_whenRoleIsInvalid() throws Exception {
        // Arrange
        Document document = new Document(DocumentType.NATIONAL_ID, "222.333.444-55", Country.BRAZIL);
        PersonalData personalData = new PersonalData("Pedro", "Alves", LocalDate.of(1995, 1, 10), document, "21912345678");
        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setRelationship("Irmã");
        emergencyContact.setFullName("Ana Alves");
        emergencyContact.setPhone("21987654321");
        PatientCreationDTO creationDTO = new PatientCreationDTO(personalData, emergencyContact, null);

        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/v1/patients/search - Deve retornar pacientes pelo primeiro nome")
    @WithMockUser(roles = "CLINICAL")
    void searchByName_shouldReturnPatientList() throws Exception {
        Document document = new Document(DocumentType.NATIONAL_ID, "333.444.555-66", Country.BRAZIL);
        PersonalData personalData = new PersonalData("Carlos", "Santana", LocalDate.of(1970, 3, 3), document, "11111-2222");
        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setFullName("Joana Santana");
        emergencyContact.setRelationship("Esposa");
        emergencyContact.setPhone("3333-4444");
        Patient patientToSave = new Patient();
        patientToSave.setPersonalData(personalData);
        patientToSave.setEmergencyContact(emergencyContact);
        patientRepository.save(patientToSave);

        mockMvc.perform(get("/api/v1/patients/search")
                        .param("firstName", "Carlos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].fullName").value("Carlos Santana"));
    }
}