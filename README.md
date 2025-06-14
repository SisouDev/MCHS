# 🧠 MHC-PMS - Mental Health Care Patient Management System

> A medical information system specialized in tracking and treating patients with mental health disorders.  
> Based on the case study from *Software Engineering* by Ian Sommerville.

---

## 🔎 About the Project

**MHC-PMS** is a clinical and administrative support system for mental health care services. It is designed to address the unique challenges of mental health treatment, such as:

- High patient turnover and disorganization
- Involuntary hospitalization with legal requirements
- Strict confidentiality and privacy regulations
- Multidisciplinary professional teams

The system must ensure fast, secure, auditable access to critical information and work both online and offline, with strong encryption.

---

## 🎯 Main Objectives

- **Clinical Support**: Provide real-time data to doctors, psychologists, nurses, and social workers.
- **Administrative and Legal Support**: Manage financial, legal, and operational records, including treatment costs and hospitalization.
- **Public Health Reporting**: Generate anonymous reports for public health policy and analysis.

---

## 🏥 Target Users and Roles

- 👩‍⚕️ **Health Professionals**: physicians, nurses, psychologists, social workers.
- 🧾 **Administrative Staff**: receptionists, schedulers, clinic managers.
- 🏠 **Community Health Agents**: responsible for home visits and patient tracking.
- 🧑‍💼 **Public Health Managers**: receive anonymized statistical reports.

---

## 🔧 Key Features (In Progress)

### 🧍 Patient Records
- Full name, date of birth
- Address history, contact information
- Legal document numbers (e.g., ID, health card)
- Diagnoses (past and current)
- Prescribed medications and treatment history

### 🩺 Clinical Sessions
- Session date and time
- Responsible professional
- Clinical notes and updated diagnosis
- Treatment plan adjustments and prescriptions

### 📋 Treatment Plans
- Therapeutic goals and strategies
- Medication, therapy, and activity schedules
- Responsible supervisor and timeframes

### 📡 Patient Monitoring
- Alerts for missed appointments
- No contact for predefined periods
- Reported behavior changes or crises

### 🛑 Involuntary Hospitalization Control
- Admission dates and legal authorizations
- Periodic review logs
- Compliance with legal and ethical standards

### 🚨 Crisis Event Management
- Description of critical events (e.g., suicide attempt)
- Emergency actions taken
- Responsible professional and outcomes

### 📊 Administrative Reports
- Number of active patients
- Consultation and no-show rates
- Hospitalization statistics
- Financial reports (medications, treatments)
- Anonymized statistics for public analysis

---

## 🔒 Security and Legal Compliance

The system handles highly sensitive data and must comply with:

- Data Protection Laws (e.g., GDPR, LGPD)
- Mental Health Laws (involuntary treatment, judicial reviews)

**Key design decisions:**

- Role-based access control and authentication
- Encrypted local data replication for offline access
- Full audit logs for legal traceability

---

## 💻 Planned Tech Stack (To Be Defined)

> *Fill in as the system evolves*

- Language: Java / Kotlin / ...
- Backend: Spring Boot / ...
- Database: PostgreSQL / H2 (testing) / ...
- Security: JWT / OAuth2 / ...
- Tools: Flyway, Lombok, MapStruct, etc.

---

## 🚧 Project Status

📌 Initial domain modeling in progress  
📌 Entity and service architecture under construction  
📌 Requirement gathering ongoing

---

## 📚 References

- Sommerville, Ian. *Software Engineering*, 10th Edition.
- Brazilian Mental Health Law (Law No. 10.216/2001)
- General Data Protection Law (LGPD - Law No. 13.709/2018)

---

## 🤝 Contributing

Feel free to open issues for suggestions or bug reports. Pull requests are welcome!

---

## 📄 License

> *(To be defined — e.g., MIT, GPL, etc.)*

---
