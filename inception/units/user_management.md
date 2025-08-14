# Unit 1: User Management

## Overview
This unit handles user registration, authentication, and profile management for both patients and doctors.

## User Stories

### US-P001: Patient Registration
**Story**: As a patient, I want to register my profile so that I can access the appointment booking system.
**Acceptance Criteria**: 
- Patient can create account with email, password, name, phone, date of birth
- Email verification required
- Profile information is securely stored

### US-P002: Patient Login
**Story**: As a patient, I want to log in with my credentials so that I can access my account.
**Acceptance Criteria**:
- Login with email/password
- Session management
- Password reset functionality

### US-P003: Patient Profile Management
**Story**: As a patient, I want to manage my profile so that my information stays current.
**Acceptance Criteria**:
- Edit personal information
- Update contact details
- Change password

### US-D001: Doctor Registration
**Story**: As a doctor, I want to register my profile so that patients can find and book appointments with me.
**Acceptance Criteria**:
- Create account with credentials, specialties, qualifications
- Upload profile photo and documents
- Set consultation fees

### US-D002: Doctor Login
**Story**: As a doctor, I want to log in with my credentials so that I can access my account.
**Acceptance Criteria**:
- Login with email/password
- Secure session management
- Password reset functionality

### US-D003: Doctor Profile Management
**Story**: As a doctor, I want to manage my profile so that patients have accurate information.
**Acceptance Criteria**:
- Edit professional information
- Update specialties and qualifications
- Manage profile visibility

## Dependencies
- None (foundational unit)

## Provides
- User authentication services
- User profile data
- User session management