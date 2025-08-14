# Appointment Booking - Logical Design

## Technology Stack
- **Frontend**: Angular 19.2.0
- **Backend**: Spring Boot 3.x with Java 21
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Security**: Spring Security with AWS Cognito
- **Event Store**: In-memory implementation

## System Architecture

### Layered Architecture
```
┌─────────────────────────────────────┐
│           Angular Frontend          │
├─────────────────────────────────────┤
│              REST API               │
├─────────────────────────────────────┤
│          Application Layer          │
├─────────────────────────────────────┤
│            Domain Layer             │
├─────────────────────────────────────┤
│         Infrastructure Layer        │
└─────────────────────────────────────┘
```

## API Design

### REST Endpoints

#### Doctor Management
- `GET /api/doctors` - List doctors with filters
- `GET /api/doctors/{id}` - Get doctor details
- `GET /api/doctors/{id}/availability` - Get doctor availability
- `POST /api/doctors/{id}/schedule` - Update doctor schedule

#### Symptom Analysis
- `POST /api/symptoms/analyze` - Analyze symptoms and get recommendations

#### Appointment Management
- `POST /api/appointments` - Book appointment
- `GET /api/appointments` - List appointments (patient/doctor view)
- `GET /api/appointments/{id}` - Get appointment details
- `PUT /api/appointments/{id}` - Reschedule appointment
- `DELETE /api/appointments/{id}` - Cancel appointment
- `POST /api/appointments/{id}/confirm` - Confirm appointment (doctor)

## Database Schema

### Tables

#### doctors
```sql
CREATE TABLE doctors (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    qualifications TEXT,
    average_rating DECIMAL(3,2) DEFAULT 0.0,
    total_reviews INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### doctor_specialties
```sql
CREATE TABLE doctor_specialties (
    id BIGINT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    specialty_type VARCHAR(100) NOT NULL,
    keywords TEXT,
    description TEXT,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);
```

#### doctor_schedules
```sql
CREATE TABLE doctor_schedules (
    id BIGINT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    day_of_week INT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    appointment_duration INT DEFAULT 30,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);
```

#### appointments
```sql
CREATE TABLE appointments (
    id BIGINT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_date_time TIMESTAMP NOT NULL,
    duration_minutes INT DEFAULT 30,
    notes TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    time_zone VARCHAR(50) DEFAULT 'UTC',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### symptom_analyses
```sql
CREATE TABLE symptom_analyses (
    id BIGINT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    keywords TEXT,
    analyzed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### recommendations
```sql
CREATE TABLE recommendations (
    id BIGINT PRIMARY KEY,
    analysis_id BIGINT NOT NULL,
    specialty VARCHAR(100) NOT NULL,
    confidence DECIMAL(3,2) NOT NULL,
    suggested_doctor_ids TEXT,
    FOREIGN KEY (analysis_id) REFERENCES symptom_analyses(id)
);
```

## Package Structure

```
src/main/java/com/healthapp/appointment/
├── domain/
│   ├── model/
│   │   ├── doctor/
│   │   │   ├── Doctor.java
│   │   │   ├── DoctorId.java
│   │   │   ├── PersonalInfo.java
│   │   │   ├── Specialty.java
│   │   │   ├── Rating.java
│   │   │   └── Schedule.java
│   │   ├── appointment/
│   │   │   ├── Appointment.java
│   │   │   ├── AppointmentId.java
│   │   │   ├── AppointmentDetails.java
│   │   │   └── AppointmentStatus.java
│   │   └── symptom/
│   │       ├── SymptomAnalysis.java
│   │       ├── SymptomDescription.java
│   │       └── Recommendation.java
│   ├── service/
│   │   ├── AppointmentBookingService.java
│   │   ├── SymptomAnalysisService.java
│   │   └── DoctorMatchingService.java
│   ├── repository/
│   │   ├── DoctorRepository.java
│   │   ├── AppointmentRepository.java
│   │   └── SymptomAnalysisRepository.java
│   └── event/
│       ├── AppointmentBooked.java
│       ├── AppointmentConfirmed.java
│       └── SymptomAnalyzed.java
├── application/
│   ├── service/
│   │   ├── AppointmentApplicationService.java
│   │   ├── DoctorApplicationService.java
│   │   └── SymptomApplicationService.java
│   └── dto/
│       ├── AppointmentDto.java
│       ├── DoctorDto.java
│       └── SymptomAnalysisDto.java
├── infrastructure/
│   ├── repository/
│   │   ├── JpaDoctorRepository.java
│   │   ├── JpaAppointmentRepository.java
│   │   └── JpaSymptomAnalysisRepository.java
│   ├── event/
│   │   ├── InMemoryEventStore.java
│   │   └── EventPublisher.java
│   └── external/
│       ├── AiSymptomAnalyzer.java
│       └── UserServiceClient.java
└── web/
    ├── controller/
    │   ├── AppointmentController.java
    │   ├── DoctorController.java
    │   └── SymptomController.java
    └── config/
        ├── SecurityConfig.java
        └── WebConfig.java
```

## Event-Driven Patterns

### Domain Events
- Events published when aggregate state changes
- In-memory event store for demo
- Event handlers for cross-aggregate communication

### Event Flow
```
SymptomAnalyzed → RecommendationsGenerated
AppointmentBooked → NotificationSent
AppointmentConfirmed → CalendarUpdated
```

## Security Design

### AWS Cognito Integration
- JWT token validation
- Role-based access control (PATIENT, DOCTOR)
- Secure API endpoints

### Authorization Rules
- Patients: Can only access their own appointments
- Doctors: Can access their appointments and patient profiles
- Cross-user data access prevented

## Frontend Architecture

### Angular Components
```
src/app/
├── core/
│   ├── auth/
│   ├── services/
│   └── guards/
├── shared/
│   ├── components/
│   └── models/
├── features/
│   ├── doctor-search/
│   ├── appointment-booking/
│   ├── appointment-management/
│   └── symptom-analysis/
└── layouts/
    ├── patient-layout/
    └── doctor-layout/
```

### State Management
- Angular services for state management
- RxJS for reactive programming
- HTTP interceptors for authentication

## Integration Points

### External Services
- **AI Service**: Mock implementation for symptom analysis
- **User Service**: Mock user profiles
- **Notification Service**: Event-based integration

### Data Flow
1. Patient describes symptoms → AI analysis → Doctor recommendations
2. Patient selects doctor → Check availability → Book appointment
3. Doctor confirms → Send notifications → Update calendars

## Demo Script Requirements
- Seed data for doctors and specialties
- Mock AI service responses
- Sample patient and doctor accounts
- End-to-end booking flow demonstration