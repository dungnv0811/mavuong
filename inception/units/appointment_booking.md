# Unit 2: Appointment Booking & Management

## Overview
This unit handles the core appointment booking functionality, including doctor discovery, AI-powered symptom analysis, appointment scheduling, and management.

## User Stories

### US-P004: Doctor Discovery
**Story**: As a patient, I want to view available doctors so that I can choose the right healthcare provider.
**Acceptance Criteria**:
- Display doctor list with profiles
- Show doctor specialties, ratings, availability
- Filter by specialty, location, rating

### US-P004A: AI-Powered Doctor Suggestions
**Story**: As a patient, I want to describe my symptoms so that the system can suggest appropriate doctors.
**Acceptance Criteria**:
- Enter symptom description in natural language
- AI analyzes keywords/tags from description
- System suggests relevant specialties
- Recommend doctors based on specialty match and ratings

### US-P005: Appointment Booking
**Story**: As a patient, I want to book appointments so that I can schedule my health check-up.
**Acceptance Criteria**:
- Select doctor, date, time from available slots
- Add appointment notes
- Auto-generated time slots based on doctor availability
- Prevent double-booking
- Handle time-zone differences

### US-P006: View Appointments
**Story**: As a patient, I want to view my appointments so that I can track my scheduled visits.
**Acceptance Criteria**:
- Display upcoming appointments
- Show appointment details (doctor, date, time, notes)
- Dashboard view with next appointment

### US-P007: Modify Appointments
**Story**: As a patient, I want to modify my appointments so that I can adjust my schedule.
**Acceptance Criteria**:
- Reschedule existing appointments
- Cancel appointments
- Update appointment notes

### US-P008: Appointment History
**Story**: As a patient, I want to view my appointment history so that I can track my medical visits.
**Acceptance Criteria**:
- Display past appointments
- Show appointment outcomes
- Reschedule from history if needed

### US-D004: Schedule Management
**Story**: As a doctor, I want to set up my schedule so that patients can book during my available hours.
**Acceptance Criteria**:
- Define working hours and days
- Set holidays and time off
- Configure appointment duration
- Block specific time slots

### US-D005: View Doctor Appointments
**Story**: As a doctor, I want to view my appointments so that I can manage my schedule.
**Acceptance Criteria**:
- Dashboard showing upcoming appointments
- View patient details for each appointment
- Daily/weekly/monthly calendar view

### US-D006: Appointment Confirmation
**Story**: As a doctor, I want to confirm bookings so that I can manage my patient flow.
**Acceptance Criteria**:
- Manual confirmation within 1-2 hours
- Auto-confirmation option
- Send confirmation notifications to patients

### US-D007: Patient Profile Access
**Story**: As a doctor, I want to view patient profiles so that I can prepare for appointments.
**Acceptance Criteria**:
- Access patient basic information
- View appointment history with patient
- See patient notes and previous feedback

## Dependencies
- User Management (for user authentication and profiles)

## Provides
- Appointment booking services
- Doctor availability data
- Appointment management functionality