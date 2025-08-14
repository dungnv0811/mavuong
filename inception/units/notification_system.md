# Unit 3: Notification System

## Overview
This unit handles all email notifications and reminders for both patients and doctors.

## User Stories

### US-P009: Patient Appointment Reminders
**Story**: As a patient, I want to receive appointment reminders so that I don't miss my visits.
**Acceptance Criteria**:
- Email reminders 24 hours before
- Email reminders 2 hours before
- Configurable notification preferences

### US-D008: Doctor Appointment Reminders
**Story**: As a doctor, I want to receive appointment reminders so that I can prepare for patient visits.
**Acceptance Criteria**:
- Email reminders 24 hours before
- Email reminders 2 hours before
- New booking notifications

## Dependencies
- Appointment Booking & Management (for appointment data)
- User Management (for user contact information)

## Provides
- Email notification services
- Reminder scheduling
- Notification preference management