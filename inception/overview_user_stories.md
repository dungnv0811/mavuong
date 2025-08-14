# Health Check-up Appointment Scheduling System - User Stories

## Patient User Stories

### Profile Management
**US-P001**: As a patient, I want to register my profile so that I can access the appointment booking system.
- **Acceptance Criteria**: 
  - Patient can create account with email, password, name, phone, date of birth
  - Email verification required
  - Profile information is securely stored

**US-P002**: As a patient, I want to log in with my credentials so that I can access my account.
- **Acceptance Criteria**:
  - Login with email/password
  - Session management
  - Password reset functionality

**US-P003**: As a patient, I want to manage my profile so that my information stays current.
- **Acceptance Criteria**:
  - Edit personal information
  - Update contact details
  - Change password

### Doctor Discovery & Booking
**US-P004**: As a patient, I want to view available doctors so that I can choose the right healthcare provider.
- **Acceptance Criteria**:
  - Display doctor list with profiles
  - Show doctor specialties, ratings, availability
  - Filter by specialty, location, rating

**US-P004A**: As a patient, I want to describe my symptoms so that the system can suggest appropriate doctors.
- **Acceptance Criteria**:
  - Enter symptom description in natural language
  - AI analyzes keywords/tags from description
  - System suggests relevant specialties
  - Recommend doctors based on specialty match and ratings

**US-P005**: As a patient, I want to book appointments so that I can schedule my health check-up.
- **Acceptance Criteria**:
  - Select doctor, date, time from available slots
  - Add appointment notes
  - Auto-generated time slots based on doctor availability
  - Prevent double-booking
  - Handle time-zone differences

### Appointment Management
**US-P006**: As a patient, I want to view my appointments so that I can track my scheduled visits.
- **Acceptance Criteria**:
  - Display upcoming appointments
  - Show appointment details (doctor, date, time, notes)
  - Dashboard view with next appointment

**US-P007**: As a patient, I want to modify my appointments so that I can adjust my schedule.
- **Acceptance Criteria**:
  - Reschedule existing appointments
  - Cancel appointments
  - Update appointment notes

**US-P008**: As a patient, I want to view my appointment history so that I can track my medical visits.
- **Acceptance Criteria**:
  - Display past appointments
  - Show appointment outcomes
  - Reschedule from history if needed

### Notifications & Reminders
**US-P009**: As a patient, I want to receive appointment reminders so that I don't miss my visits.
- **Acceptance Criteria**:
  - Email reminders 24 hours before
  - Email reminders 2 hours before
  - Configurable notification preferences

### Rating & Feedback
**US-P010**: As a patient, I want to rate and provide feedback for doctors so that I can share my experience.
- **Acceptance Criteria**:
  - Rate doctors (1-5 stars)
  - Write text feedback
  - Submit after appointment completion

### Smart Recommendations
**US-P011**: As a patient, I want to receive doctor recommendations so that I can find the best healthcare providers.
- **Acceptance Criteria**:
  - Suggest high-rated doctors based on specialty needs
  - Consider patient's rating history
  - Factor in recent appointments (last 2 months)
  - Push personalized recommendations

## Doctor User Stories

### Profile Management
**US-D001**: As a doctor, I want to register my profile so that patients can find and book appointments with me.
- **Acceptance Criteria**:
  - Create account with credentials, specialties, qualifications
  - Upload profile photo and documents
  - Set consultation fees

**US-D002**: As a doctor, I want to log in with my credentials so that I can access my account.
- **Acceptance Criteria**:
  - Login with email/password
  - Secure session management
  - Password reset functionality

**US-D003**: As a doctor, I want to manage my profile so that patients have accurate information.
- **Acceptance Criteria**:
  - Edit professional information
  - Update specialties and qualifications
  - Manage profile visibility

### Schedule Management
**US-D004**: As a doctor, I want to set up my schedule so that patients can book during my available hours.
- **Acceptance Criteria**:
  - Define working hours and days
  - Set holidays and time off
  - Configure appointment duration
  - Block specific time slots

### Appointment Management
**US-D005**: As a doctor, I want to view my appointments so that I can manage my schedule.
- **Acceptance Criteria**:
  - Dashboard showing upcoming appointments
  - View patient details for each appointment
  - Daily/weekly/monthly calendar view

**US-D006**: As a doctor, I want to confirm bookings so that I can manage my patient flow.
- **Acceptance Criteria**:
  - Manual confirmation within 1-2 hours
  - Auto-confirmation option
  - Send confirmation notifications to patients

**US-D007**: As a doctor, I want to view patient profiles so that I can prepare for appointments.
- **Acceptance Criteria**:
  - Access patient basic information
  - View appointment history with patient
  - See patient notes and previous feedback

### Notifications
**US-D008**: As a doctor, I want to receive appointment reminders so that I can prepare for patient visits.
- **Acceptance Criteria**:
  - Email reminders 24 hours before
  - Email reminders 2 hours before
  - New booking notifications

## System Features

### Health Insights
**US-S001**: As a user, I want to see health insights so that I can make informed healthcare decisions.
- **Acceptance Criteria**:
  - Display health suggestions based on appointment history
  - Show trends in health check-ups
  - Provide specialty-specific recommendations

### Dashboard
**US-S002**: As a user, I want a dashboard view so that I can quickly access important information.
- **Acceptance Criteria**:
  - Patient dashboard: upcoming appointments, recommendations, health insights
  - Doctor dashboard: today's appointments, pending confirmations, patient summaries