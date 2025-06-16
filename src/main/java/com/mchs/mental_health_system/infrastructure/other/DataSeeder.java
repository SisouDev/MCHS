package com.mchs.mental_health_system.infrastructure.other;

import com.mchs.mental_health_system.application.dto.clinicalRecords.DiagnosisRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.PrescriptionRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.TreatmentPlanRequestDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.AdmissionRequestDTO;
import com.mchs.mental_health_system.application.dto.hospitalization.AlertRequestDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientCreationDTO;
import com.mchs.mental_health_system.application.dto.patient.PatientResponseDTO;
import com.mchs.mental_health_system.application.dto.user.AdministrativeProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.HealthProfessionalResponseDTO;
import com.mchs.mental_health_system.application.dto.user.UserCreationRequestDTO;
import com.mchs.mental_health_system.application.services.clinicalRecords.ConsultationSessionService;
import com.mchs.mental_health_system.application.services.clinicalRecords.DiagnosisService;
import com.mchs.mental_health_system.application.services.clinicalRecords.TreatmentPlanService;
import com.mchs.mental_health_system.application.services.hospitalization.AdmissionService;
import com.mchs.mental_health_system.application.services.hospitalization.AlertService;
import com.mchs.mental_health_system.application.services.patient.PatientService;
import com.mchs.mental_health_system.application.services.user.UserService;
import com.mchs.mental_health_system.domain.model.entities.clinicalRecords.ConsultationSession;
import com.mchs.mental_health_system.domain.model.entities.facility.CareFacility;
import com.mchs.mental_health_system.domain.model.entities.patient.Patient;
import com.mchs.mental_health_system.domain.model.entities.user.HealthProfessional;
import com.mchs.mental_health_system.domain.model.entities.user.SystemUser;
import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.TreatmentPlanStatus;
import com.mchs.mental_health_system.domain.model.enums.hospitalization.AdmissionType;
import com.mchs.mental_health_system.domain.model.enums.hospitalization.AlertType;
import com.mchs.mental_health_system.domain.model.enums.others.Country;
import com.mchs.mental_health_system.domain.model.enums.others.DocumentType;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AccessProfile;
import com.mchs.mental_health_system.domain.model.enums.userManagement.AdministrativeRole;
import com.mchs.mental_health_system.domain.model.enums.userManagement.MedicalSpecialty;
import com.mchs.mental_health_system.domain.model.shared.embeddable.Address;
import com.mchs.mental_health_system.domain.model.shared.embeddable.Document;
import com.mchs.mental_health_system.domain.model.shared.embeddable.EmergencyContact;
import com.mchs.mental_health_system.domain.model.shared.embeddable.PersonalData;
import com.mchs.mental_health_system.domain.repositories.clinicalRecords.ConsultationSessionRepository;
import com.mchs.mental_health_system.domain.repositories.facility.CareFacilityRepository;
import com.mchs.mental_health_system.domain.repositories.patient.PatientRepository;
import com.mchs.mental_health_system.domain.repositories.user.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final CareFacilityRepository careFacilityRepository;
    private final SystemUserRepository systemUserRepository;
    private final UserService userService;
    private final PatientRepository patientRepository;
    private final PatientService patientService;
    private final AdmissionService admissionService;
    private final AlertService alertService;
    private final ConsultationSessionService consultationSessionService;
    private final DiagnosisService diagnosisService;
    private final TreatmentPlanService treatmentPlanService;
    private final ConsultationSessionRepository consultationSessionRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data seeding...");

        if (!systemUserRepository.existsByUsername("ana.souza")) {
            log.info("Database appears to be empty. Starting data seeding...");

            List<CareFacility> facilities = seedCareFacilities();
            List<SystemUser> users = seedUsers(facilities);
            List<Patient> patients = seedPatients(facilities);
            seedHospitalizationData(patients, users);
            seedClinicalRecords(patients, users);

            log.info("Data seeding finished successfully.");
        } else {
            log.info("Key user 'ana.souza' already exists. Skipping data seeding.");
        }

        log.info("Data seeding finished.");
    }

    private List<CareFacility> seedCareFacilities() {
        log.info("Seeding Care Facilities...");

        CareFacility facility1 = new CareFacility();
        facility1.setName("Clínica Mente Saudável");
        facility1.setAddress(new Address("Rua das Flores, 123", "São Paulo", "SP", "01000-000", Country.BRAZIL));
        facility1.setPrimaryEmail("contato@mentesaudavel.com");
        facility1.setPrimaryPhone("11-1234-5678");

        CareFacility facility2 = new CareFacility();
        facility2.setName("Espaço Serenidade");
        facility2.setAddress(new Address("Avenida da Paz, 456", "Rio de Janeiro", "RJ", "22000-000", Country.BRAZIL));
        facility2.setPrimaryEmail("paz@serenidade.com.br");
        facility2.setPrimaryPhone("21-9876-5432");

        List<CareFacility> savedFacilities = careFacilityRepository.saveAll(List.of(facility1, facility2));

        log.info("Care Facilities seeded: {} facilities created.", savedFacilities.size());
        return savedFacilities;
    }

    private List<SystemUser> seedUsers(List<CareFacility> facilities) {
        log.info("Seeding Users...");
        List<SystemUser> savedUsers = new ArrayList<>();
        Long facilityId1 = facilities.get(0).getId();
        Long facilityId2 = facilities.get(1).getId();

        UserCreationRequestDTO drAnaRequest = new UserCreationRequestDTO(
                "ana.souza", "ana.souza@mhc.com", "senha123",
                AccessProfile.ROLE_CLINICAL,
                new PersonalData("Ana", "Souza", LocalDate.of(1985, 5, 15),
                        new Document(DocumentType.NATIONAL_ID, "111.222.333-44", Country.BRAZIL),
                        "11-9999-0001"),
                MedicalSpecialty.PSYCHIATRIST, "CRM-SP/123456", null, facilityId1
        );
        HealthProfessionalResponseDTO drAnaResponse = userService.createHealthProfessional(drAnaRequest);
        systemUserRepository.findById(drAnaResponse.id()).ifPresent(savedUsers::add);

        UserCreationRequestDTO drCarlosRequest = new UserCreationRequestDTO(
                "carlos.mendes", "carlos.mendes@mhc.com", "senha123",
                AccessProfile.ROLE_CLINICAL,
                new PersonalData("Carlos", "Mendes", LocalDate.of(1990, 8, 22),
                        new Document(DocumentType.NATIONAL_ID, "222.333.444-55", Country.BRAZIL),
                        "21-9888-0002"),
                MedicalSpecialty.PSYCHOLOGIST, "CRP-RJ/789012", null, facilityId2
        );
        HealthProfessionalResponseDTO drCarlosResponse = userService.createHealthProfessional(drCarlosRequest);
        systemUserRepository.findById(drCarlosResponse.id()).ifPresent(savedUsers::add);

        UserCreationRequestDTO joaoRequest = new UserCreationRequestDTO(
                "joao.silva", "joao.silva@mhc.com", "senha123",
                AccessProfile.ROLE_ADMINISTRATIVE,
                new PersonalData("João", "Silva", LocalDate.of(1995, 3, 10),
                        new Document(DocumentType.NATIONAL_ID, "333.444.555-66", Country.BRAZIL),
                        "11-9888-0003"),
                null, null, AdministrativeRole.RECEPTIONIST, facilityId1
        );
        AdministrativeProfessionalResponseDTO joaoResponse = userService.createAdministrativeProfessional(joaoRequest);
        systemUserRepository.findById(joaoResponse.id()).ifPresent(savedUsers::add);

        UserCreationRequestDTO mariaRequest = new UserCreationRequestDTO(
                "maria.lima", "maria.lima@mhc.com", "senha123",
                AccessProfile.ROLE_CLINICAL,
                new PersonalData("Maria", "Lima", LocalDate.of(1992, 2, 18),
                        new Document(DocumentType.NATIONAL_ID, "999.888.777-66", Country.BRAZIL),
                        "21-9777-0004"),
                MedicalSpecialty.NURSE, "COREN-RJ/345678", null, facilityId2
        );
        HealthProfessionalResponseDTO mariaResponse = userService.createHealthProfessional(mariaRequest);
        systemUserRepository.findById(mariaResponse.id()).ifPresent(savedUsers::add);

        UserCreationRequestDTO ricardoRequest = new UserCreationRequestDTO(
                "ricardo.alves", "ricardo.alves@mhc.com", "senha123",
                AccessProfile.ROLE_ADMINISTRATIVE,
                new PersonalData("Ricardo", "Alves", LocalDate.of(1980, 12, 1),
                        new Document(DocumentType.NATIONAL_ID, "123.123.123-12", Country.BRAZIL),
                        "11-9666-0005"),
                null, null, AdministrativeRole.CLINIC_MANAGER, facilityId1
        );
        AdministrativeProfessionalResponseDTO ricardoResponse = userService.createAdministrativeProfessional(ricardoRequest);
        systemUserRepository.findById(ricardoResponse.id()).ifPresent(savedUsers::add);

        UserCreationRequestDTO fernandaRequest = new UserCreationRequestDTO(
                "fernanda.costa", "fernanda.costa@mhc.com", "senha123",
                AccessProfile.ROLE_CLINICAL,
                new PersonalData("Fernanda", "Costa", LocalDate.of(1989, 6, 30),
                        new Document(DocumentType.NATIONAL_ID, "456.456.456-45", Country.BRAZIL),
                        "11-9555-0006"),
                MedicalSpecialty.SOCIAL_WORKER, "CRESS-SP/98765", null, facilityId1
        );
        HealthProfessionalResponseDTO fernandaResponse = userService.createHealthProfessional(fernandaRequest);
        systemUserRepository.findById(fernandaResponse.id()).ifPresent(savedUsers::add);

        log.info("Users seeded.");
        return savedUsers;
    }

    private List<Patient> seedPatients(List<CareFacility> facilities) {
        log.info("Seeding Patients...");
        List<Patient> savedPatients = new ArrayList<>();
        Long facilityId1 = facilities.get(0).getId();
        Long facilityId2 = facilities.get(1).getId();

        PersonalData brunoData = new PersonalData(
                "Bruno", "Gomes", LocalDate.of(1992, 10, 20),
                new Document(DocumentType.NATIONAL_ID, "444.555.666-77", Country.BRAZIL),
                "11-9777-0004"
        );

        EmergencyContact brunoEmergency = new EmergencyContact();
        brunoEmergency.setFullName("Maria Gomes");
        brunoEmergency.setRelationship("Mãe");
        brunoEmergency.setPhone("11-9777-0005");

        PatientCreationDTO brunoRequest = new PatientCreationDTO(brunoData, brunoEmergency, facilityId1);
        PatientResponseDTO brunoResponse = patientService.registerNewPatient(brunoRequest);
        patientRepository.findById(brunoResponse.id()).ifPresent(savedPatients::add);

        PersonalData carlaData = new PersonalData(
                "Carla", "Fernandes", LocalDate.of(1988, 1, 30),
                new Document(DocumentType.NATIONAL_ID, "555.666.777-88", Country.BRAZIL),
                "21-9666-0006"
        );

        EmergencyContact carlaEmergency = new EmergencyContact();
        carlaEmergency.setFullName("Pedro Fernandes");
        carlaEmergency.setRelationship("Irmão");
        carlaEmergency.setPhone("21-9666-0007");

        PatientCreationDTO carlaRequest = new PatientCreationDTO(carlaData, carlaEmergency, facilityId2);

        PatientResponseDTO carlaResponse = patientService.registerNewPatient(carlaRequest);
        patientRepository.findById(carlaResponse.id()).ifPresent(savedPatients::add);

        PersonalData sofiaData = new PersonalData(
                "Sofia", "Oliveira", LocalDate.of(1995, 7, 12),
                new Document(DocumentType.NATIONAL_ID, "666.777.888-99", Country.BRAZIL),
                "11-9555-0008"
        );
        EmergencyContact sofiaEmergency = new EmergencyContact();
        sofiaEmergency.setFullName("Jorge Oliveira");
        sofiaEmergency.setRelationship("Pai");
        sofiaEmergency.setPhone("11-9555-0009");
        PatientCreationDTO sofiaRequest = new PatientCreationDTO(sofiaData, sofiaEmergency, facilityId1);
        PatientResponseDTO sofiaResponse = patientService.registerNewPatient(sofiaRequest);
        patientRepository.findById(sofiaResponse.id()).ifPresent(savedPatients::add);

        PersonalData lucasData = new PersonalData(
                "Lucas", "Pereira", LocalDate.of(2001, 11, 5),
                new Document(DocumentType.NATIONAL_ID, "777.888.999-00", Country.BRAZIL),
                "21-9444-0010"
        );
        EmergencyContact lucasEmergency = new EmergencyContact();
        lucasEmergency.setFullName("Juliana Pereira");
        lucasEmergency.setRelationship("Irmã");
        lucasEmergency.setPhone("21-9444-0011");
        PatientCreationDTO lucasRequest = new PatientCreationDTO(lucasData, lucasEmergency, facilityId2);
        PatientResponseDTO lucasResponse = patientService.registerNewPatient(lucasRequest);
        patientRepository.findById(lucasResponse.id()).ifPresent(savedPatients::add);

        PersonalData isabelaData = new PersonalData(
                "Isabela", "Costa", LocalDate.of(1979, 2, 25),
                new Document(DocumentType.NATIONAL_ID, "888.999.000-11", Country.BRAZIL),
                "11-9333-0012"
        );
        EmergencyContact isabelaEmergency = new EmergencyContact();
        isabelaEmergency.setFullName("Ricardo Costa");
        isabelaEmergency.setRelationship("Marido");
        isabelaEmergency.setPhone("11-9333-0013");
        PatientCreationDTO isabelaRequest = new PatientCreationDTO(isabelaData, isabelaEmergency, facilityId1);
        PatientResponseDTO isabelaResponse = patientService.registerNewPatient(isabelaRequest);
        patientRepository.findById(isabelaResponse.id()).ifPresent(savedPatients::add);

        log.info("Patients seeded.");
        return savedPatients;

    }

    private void seedHospitalizationData(List<Patient> patients, List<SystemUser> users) {
        log.info("Seeding Hospitalization Data (Admissions and Alerts)...");

        Patient bruno = patients.get(0);
        Patient carla = patients.get(1);
        Patient sofia = patients.get(2);

        HealthProfessional drAna = (HealthProfessional) users.stream()
                .filter(u -> u.getUsername().equals("ana.souza"))
                .findFirst().orElseThrow();

        HealthProfessional drCarlos = (HealthProfessional) users.stream()
                .filter(u -> u.getUsername().equals("carlos.mendes"))
                .findFirst().orElseThrow();

        HealthProfessional fernanda = (HealthProfessional) users.stream()
                .filter(u -> u.getUsername().equals("fernanda.costa"))
                .findFirst().orElseThrow();


        AdmissionRequestDTO brunoAdmissionRequest = new AdmissionRequestDTO(
                bruno.getId(),
                AdmissionType.VOLUNTARY,
                LocalDateTime.now().minusDays(10),
                "Episódio depressivo agudo com sintomas psicóticos.",
                "Ala A - Quarto 101"
        );
        admissionService.admitPatient(bruno.getId(), brunoAdmissionRequest);
        log.info("Admission created for patient: " + bruno.getPersonalData().getFullName());

        AlertRequestDTO carlaAlertRequest = new AlertRequestDTO(
                AlertType.MEDICATION_NON_ADHERENCE,
                "Paciente relatou ter esquecido de tomar a medicação por 2 dias. Risco de recaída.",
                drAna.getId()
        );
        alertService.createAlert(carla.getId(), carlaAlertRequest);
        log.info("Alert created for patient: " + carla.getPersonalData().getFullName() + ", assigned to: " + drAna.getPersonalData().getFullName());


        AdmissionRequestDTO carlaAdmissionRequest = new AdmissionRequestDTO(
                carla.getId(),
                AdmissionType.INVOLUNTARY,
                LocalDateTime.now().minusDays(20),
                "Crise de ansiedade severa com agorafobia, recusando-se a sair de casa.",
                "Ala B - Quarto 203"
        );
        admissionService.admitPatient(carla.getId(), carlaAdmissionRequest);
        log.info("Admission created for patient: " + carla.getPersonalData().getFullName());

        AlertRequestDTO brunoAlertRequest = new AlertRequestDTO(
                AlertType.BEHAVIORAL_CHANGE,
                "Paciente Bruno apresentou agitação e comportamento hostil com a equipe de enfermagem durante a noite.",
                drCarlos.getId()
        );
        alertService.createAlert(bruno.getId(), brunoAlertRequest);
        log.info("Alert created for patient: " + bruno.getPersonalData().getFullName() + ", assigned to: " + drCarlos.getPersonalData().getFullName());

        AlertRequestDTO sofiaAlertRequest = new AlertRequestDTO(
                AlertType.NO_CONTACT,
                "Tentativas de contato com a paciente Sofia por 3 dias sem resposta. Visita domiciliar recomendada.",
                fernanda.getId()
        );
        alertService.createAlert(sofia.getId(), sofiaAlertRequest);
        log.info("Alert created for patient: " + sofia.getPersonalData().getFullName() + ", assigned to: " + fernanda.getPersonalData().getFullName());
    }

    private void seedClinicalRecords(List<Patient> patients, List<SystemUser> users) {
        log.info("Seeding Clinical Records...");

        Patient bruno = patients.get(0);
        Patient carla = patients.get(1);
        Patient sofia = patients.get(2);
        HealthProfessional drAna = (HealthProfessional) users.stream()
                .filter(u -> u.getUsername().equals("ana.souza"))
                .findFirst().orElseThrow();
        HealthProfessional drCarlos = (HealthProfessional) users.stream()
                .filter(u -> u.getUsername().equals("carlos.mendes"))
                .findFirst().orElseThrow();

        HealthProfessional fernanda = (HealthProfessional) users.stream().filter(u -> u.getUsername().equals("fernanda.costa")).findFirst().orElseThrow();

        TreatmentPlanRequestDTO brunoPlanRequest = new TreatmentPlanRequestDTO(
                bruno.getId(),
                drAna.getId(),
                TreatmentPlanStatus.ACTIVE,
                "Reduzir sintomas depressivos e melhorar o funcionamento social.",
                LocalDate.now().minusDays(10),
                LocalDate.now().plusMonths(6)
        );
        treatmentPlanService.createTreatmentPlan(bruno.getId(), brunoPlanRequest);
        log.info("Treatment Plan created for patient: " + bruno.getPersonalData().getFullName());

        DiagnosisRequestDTO brunoDiagnosisRequest = new DiagnosisRequestDTO(
                bruno.getId(),
                "F32.2",
                "Episódio depressivo grave sem sintomas psicóticos",
                LocalDate.now().minusDays(10),
                true
        );
        diagnosisService.addDiagnosis(bruno.getId(), brunoDiagnosisRequest);
        log.info("Diagnosis created for patient: " + bruno.getPersonalData().getFullName());

        log.info("Creating historical Consultation Session for patient Bruno...");

        ConsultationSession brunoSession = new ConsultationSession();
        brunoSession.setPatient(bruno);
        brunoSession.setHealthProfessional(drAna);
        brunoSession.setSessionDateTime(LocalDateTime.now().minusDays(5));
        brunoSession.setClinicalNotes("Paciente reporta melhora no humor, mas ainda com dificuldades de sono. Discutido ajuste de medicação.");

        ConsultationSession savedBrunoSession = consultationSessionRepository.save(brunoSession);
        log.info("Consultation Session created with ID: " + savedBrunoSession.getId());

        PrescriptionRequestDTO brunoPrescription = new PrescriptionRequestDTO(
                "Sertralina", "50mg", "Tomar 1 comprimido pela manhã.",
                LocalDate.now().minusDays(5), 30
        );
        consultationSessionService.addPrescription(savedBrunoSession.getId(), brunoPrescription);
        log.info("Prescription added to session ID: " + savedBrunoSession.getId());

        ConsultationSession brunoSession2 = new ConsultationSession();
        brunoSession2.setPatient(bruno);
        brunoSession2.setHealthProfessional(drAna);
        brunoSession2.setSessionDateTime(LocalDateTime.now().minusDays(2));
        brunoSession2.setClinicalNotes("Paciente reporta melhora no humor, mas ainda com dificuldades de sono. Discutido ajuste de medicação e encaminhamento para terapia.");
        consultationSessionRepository.save(brunoSession2);
        log.info("Follow-up Consultation (2) created for patient: Bruno Gomes");


        TreatmentPlanRequestDTO carlaPlanRequest = new TreatmentPlanRequestDTO(carla.getId(), drCarlos.getId(), TreatmentPlanStatus.ACTIVE, "Desenvolver estratégias de enfrentamento para ansiedade e agorafobia.", LocalDate.now().minusDays(15), LocalDate.now().plusMonths(4));
        treatmentPlanService.createTreatmentPlan(carla.getId(), carlaPlanRequest);

        DiagnosisRequestDTO carlaDiagnosisRequest = new DiagnosisRequestDTO(carla.getId(), "F41.1", "Transtorno de ansiedade generalizada", LocalDate.now().minusDays(15), true);
        diagnosisService.addDiagnosis(carla.getId(), carlaDiagnosisRequest);

        ConsultationSession carlaSession = new ConsultationSession();
        carlaSession.setPatient(carla);
        carlaSession.setHealthProfessional(drCarlos);
        carlaSession.setSessionDateTime(LocalDateTime.now().minusDays(7));
        carlaSession.setClinicalNotes("Sessão de terapia cognitivo-comportamental focada em técnicas de relaxamento e exposição gradual.");
        consultationSessionRepository.save(carlaSession);
        log.info("Records and Consultation created for patient: Carla Fernandes");

        DiagnosisRequestDTO sofiaDiagnosisRequest = new DiagnosisRequestDTO(sofia.getId(), "F43.1", "Transtorno de estresse pós-traumático", LocalDate.now().minusDays(5), true);
        diagnosisService.addDiagnosis(sofia.getId(), sofiaDiagnosisRequest);

        ConsultationSession sofiaSession = new ConsultationSession();
        sofiaSession.setPatient(sofia);
        sofiaSession.setHealthProfessional(fernanda);
        sofiaSession.setSessionDateTime(LocalDateTime.now().minusDays(3));
        sofiaSession.setClinicalNotes("Primeiro contato. Avaliação social e familiar. Identificados pontos de apoio na rede familiar.");
        consultationSessionRepository.save(sofiaSession);
        log.info("Records and Consultation created for patient: Sofia Oliveira");

    }

}