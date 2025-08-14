# Appointment Booking Domain Model (DDD)

## Domain Overview
The Appointment Booking bounded context handles doctor discovery, AI-powered symptom analysis, appointment scheduling, and appointment management.

## Aggregates

### 1. Doctor Aggregate
**Aggregate Root**: Doctor
- **Purpose**: Manages doctor information, specialties, and availability
- **Invariants**: 
  - Doctor must have at least one specialty
  - Working hours cannot overlap with blocked time slots
  - Appointment slots must be within working hours

#### Entities
- **Doctor** (Root)
  - DoctorId (Identity)
  - PersonalInfo (Value Object)
  - Specialties (List of Specialty Value Objects)
  - Schedule (Entity)
  - Rating (Value Object)

#### Value Objects
- **PersonalInfo**: name, email, phone, qualifications
- **Specialty**: specialtyType, keywords, description
- **Rating**: averageRating, totalReviews
- **WorkingHours**: dayOfWeek, startTime, endTime
- **TimeSlot**: startDateTime, endDateTime, duration

#### Domain Events
- DoctorRegistered
- ScheduleUpdated
- AvailabilityChanged

### 2. Appointment Aggregate
**Aggregate Root**: Appointment
- **Purpose**: Manages appointment lifecycle and booking rules
- **Invariants**:
  - Cannot double-book same time slot
  - Appointment must be in future
  - Only confirmed appointments can be rescheduled

#### Entities
- **Appointment** (Root)
  - AppointmentId (Identity)
  - PatientId (Identity)
  - DoctorId (Identity)
  - AppointmentDetails (Value Object)
  - Status (Enum)

#### Value Objects
- **AppointmentDetails**: dateTime, duration, notes, timeZone
- **AppointmentStatus**: PENDING, CONFIRMED, CANCELLED, COMPLETED

#### Domain Events
- AppointmentBooked
- AppointmentConfirmed
- AppointmentCancelled
- AppointmentRescheduled
- AppointmentCompleted

### 3. Symptom Analysis Aggregate
**Aggregate Root**: SymptomAnalysis
- **Purpose**: Handles AI-powered symptom analysis and doctor recommendations
- **Invariants**:
  - Analysis must have at least one suggested specialty
  - Confidence score must be between 0-1

#### Entities
- **SymptomAnalysis** (Root)
  - AnalysisId (Identity)
  - PatientId (Identity)
  - SymptomDescription (Value Object)
  - Recommendations (List of Recommendation Value Objects)

#### Value Objects
- **SymptomDescription**: text, keywords, analyzedAt
- **Recommendation**: specialty, confidence, suggestedDoctors

#### Domain Events
- SymptomAnalyzed
- RecommendationsGenerated

## Domain Services

### 1. AppointmentBookingService
- **Purpose**: Orchestrates appointment booking process
- **Methods**:
  - bookAppointment(patientId, doctorId, timeSlot, notes)
  - checkAvailability(doctorId, dateRange)
  - rescheduleAppointment(appointmentId, newTimeSlot)

### 2. SymptomAnalysisService
- **Purpose**: Analyzes symptoms and generates recommendations
- **Methods**:
  - analyzeSymptoms(description)
  - generateRecommendations(analysis, availableDoctors)

### 3. DoctorMatchingService
- **Purpose**: Matches patients with suitable doctors
- **Methods**:
  - findDoctorsBySpecialty(specialty, filters)
  - rankDoctorsByRelevance(doctors, patientHistory)

## Repositories

### 1. DoctorRepository
- findById(doctorId)
- findBySpecialty(specialty)
- findAvailableDoctors(dateRange, specialty)
- save(doctor)

### 2. AppointmentRepository
- findById(appointmentId)
- findByPatientId(patientId)
- findByDoctorId(doctorId)
- findByDateRange(startDate, endDate)
- save(appointment)

### 3. SymptomAnalysisRepository
- findById(analysisId)
- findByPatientId(patientId)
- save(analysis)

## Domain Policies

### 1. AppointmentBookingPolicy
- **Rule**: Appointments can only be booked during doctor's working hours
- **Rule**: No double-booking allowed for same time slot
- **Rule**: Minimum 1-hour notice required for booking

### 2. AppointmentConfirmationPolicy
- **Rule**: Doctor must confirm appointment within 2 hours
- **Rule**: Auto-confirm if doctor has auto-confirmation enabled

### 3. ReschedulingPolicy
- **Rule**: Only confirmed appointments can be rescheduled
- **Rule**: Minimum 24-hour notice required for rescheduling

## Domain Events Flow

```
Patient describes symptoms → SymptomAnalyzed
↓
AI analyzes keywords → RecommendationsGenerated
↓
Patient selects doctor → AppointmentBooked
↓
Doctor confirms → AppointmentConfirmed
↓
System sends notifications
```

## Integration Points

### External Services
- **AI Service**: For symptom analysis and keyword extraction
- **User Service**: For patient and doctor profile information
- **Notification Service**: For sending appointment confirmations and reminders

### Anti-Corruption Layer
- **UserProfileAdapter**: Translates user service data to domain objects
- **NotificationAdapter**: Translates domain events to notification commands