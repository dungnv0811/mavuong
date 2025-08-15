import { Injectable, inject } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay, switchMap } from 'rxjs/operators';
import { DoctorBookingService } from './doctor-booking.service';
import { DoctorProfile } from '../../shared/models/doctor.model';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  private doctorBookingService = inject(DoctorBookingService);

  private mockDoctorProfiles: { [key: string]: any } = {
    'doc-001': {
      firstName: 'Elena', lastName: 'Rodriguez', email: 'elena.rodriguez@hospital.com', phone: '(555) 001-0001',
      professionalTitle: 'MD, Board Certified General Practitioner', yearsExperience: 15,
      biography: 'Dr. Elena Rodriguez is a dedicated general practitioner with over 15 years of experience in family medicine.',
      medicalLicenseNumber: 'GP001234', certifications: 'M.D., Harvard Medical School, 2009\nBoard Certified Family Medicine, 2012',
      memberships: 'American Academy of Family Physicians\nAmerican Medical Association'
    },
    'doc-002': {
      firstName: 'Michael', lastName: 'Chen', email: 'michael.chen@hospital.com', phone: '(555) 002-0002',
      professionalTitle: 'MD, Board Certified Pediatrician', yearsExperience: 12,
      biography: 'Dr. Michael Chen specializes in pediatric care with a focus on child development and preventive medicine.',
      medicalLicenseNumber: 'PD002345', certifications: 'M.D., Stanford Medical School, 2012\nBoard Certified Pediatrics, 2015',
      memberships: 'American Academy of Pediatrics\nCalifornia Medical Association'
    },
    'doc-003': {
      firstName: 'Sarah', lastName: 'Kim', email: 'sarah.kim@hospital.com', phone: '(555) 003-0003',
      professionalTitle: 'MD, Board Certified Dermatologist', yearsExperience: 10,
      biography: 'Dr. Sarah Kim is an expert dermatologist specializing in both medical and cosmetic dermatology.',
      medicalLicenseNumber: 'DM003456', certifications: 'M.D., UCLA Medical School, 2014\nBoard Certified Dermatology, 2018',
      memberships: 'American Academy of Dermatology\nAmerican Society for Dermatologic Surgery'
    },
    'doc-004': {
      firstName: 'David', lastName: 'Green', email: 'david.green@hospital.com', phone: '(555) 004-0004',
      professionalTitle: 'MD, Board Certified Cardiologist', yearsExperience: 18,
      biography: 'Dr. David Green is a renowned cardiologist with expertise in interventional cardiology and heart disease prevention.',
      medicalLicenseNumber: 'CD004567', certifications: 'M.D., Johns Hopkins Medical School, 2006\nBoard Certified Cardiology, 2012',
      memberships: 'American College of Cardiology\nAmerican Heart Association'
    },
    'doc-005': {
      firstName: 'Emily', lastName: 'White', email: 'emily.white@hospital.com', phone: '(555) 005-0005',
      professionalTitle: 'MD, Board Certified Ophthalmologist', yearsExperience: 14,
      biography: 'Dr. Emily White is a skilled ophthalmologist specializing in retinal diseases and cataract surgery.',
      medicalLicenseNumber: 'OP005678', certifications: 'M.D., University of Pennsylvania, 2010\nBoard Certified Ophthalmology, 2014',
      memberships: 'American Academy of Ophthalmology\nAmerican Society of Retina Specialists'
    },
    'doc-006': {
      firstName: 'James', lastName: 'Brown', email: 'james.brown@hospital.com', phone: '(555) 006-0006',
      professionalTitle: 'MD, Board Certified Orthopedic Surgeon', yearsExperience: 20,
      biography: 'Dr. James Brown is an experienced orthopedic surgeon specializing in sports medicine and joint replacement.',
      medicalLicenseNumber: 'OR006789', certifications: 'M.D., Mayo Clinic Medical School, 2004\nBoard Certified Orthopedic Surgery, 2009',
      memberships: 'American Academy of Orthopedic Surgeons\nAmerican Orthopedic Society for Sports Medicine'
    },
    'doc-007': {
      firstName: 'Olivia', lastName: 'Perez', email: 'olivia.perez@hospital.com', phone: '(555) 007-0007',
      professionalTitle: 'MD, Board Certified Neurologist', yearsExperience: 13,
      biography: 'Dr. Olivia Perez is a neurologist with expertise in treating epilepsy, stroke, and neurodegenerative diseases.',
      medicalLicenseNumber: 'NR007890', certifications: 'M.D., Columbia Medical School, 2011\nBoard Certified Neurology, 2015',
      memberships: 'American Academy of Neurology\nAmerican Epilepsy Society'
    },
    'doc-008': {
      firstName: 'Ben', lastName: 'Carter', email: 'ben.carter@hospital.com', phone: '(555) 008-0008',
      professionalTitle: 'MD, Board Certified Psychiatrist', yearsExperience: 16,
      biography: 'Dr. Ben Carter is a psychiatrist specializing in anxiety disorders, depression, and cognitive behavioral therapy.',
      medicalLicenseNumber: 'PS008901', certifications: 'M.D., Yale Medical School, 2008\nBoard Certified Psychiatry, 2012',
      memberships: 'American Psychiatric Association\nAmerican Academy of Clinical Psychiatrists'
    },
    'doc-009': {
      firstName: 'Sofia', lastName: 'Garcia', email: 'sofia.garcia@hospital.com', phone: '(555) 009-0009',
      professionalTitle: 'DDS, Board Certified Dentist', yearsExperience: 11,
      biography: 'Dr. Sofia Garcia is a general dentist with expertise in restorative dentistry and oral health prevention.',
      medicalLicenseNumber: 'DT009012', certifications: 'D.D.S., University of Southern California, 2013\nBoard Certified General Dentistry, 2014',
      memberships: 'American Dental Association\nCalifornia Dental Association'
    }
  };

  getDoctorProfile(userUuid: string): Observable<DoctorProfile> {
    return this.doctorBookingService.getDoctorBookingInfo(userUuid).pipe(
      switchMap(doctorInfo => {
        if (doctorInfo) {
          const profileData = this.mockDoctorProfiles[userUuid] || {};
          const mockProfile = new DoctorProfile(
            userUuid,
            profileData.firstName || doctorInfo.name.split(' ')[1] || 'John',
            profileData.lastName || doctorInfo.name.split(' ')[2] || 'Doe',
            profileData.professionalTitle || 'MD, Board Certified ' + doctorInfo.specialty,
            doctorInfo.specialty,
            profileData.yearsExperience || 12,
            profileData.biography || `${doctorInfo.name} is a board-certified ${doctorInfo.specialty.toLowerCase()} with extensive experience...`,
            profileData.email || doctorInfo.name.toLowerCase().replace(/\s+/g, '.') + '@example.com',
            profileData.phone || '(893) 123-4567',
            {
              street: '123 Maple Street',
              city: 'Anytown',
              state: 'CA',
              postalCode: '90210',
              country: 'USA'
            },
            profileData.medicalLicenseNumber || 'MD123456',
            profileData.certifications || 'M.D., Medical School, 2016\nBoard Certified ' + doctorInfo.specialty + ', 2018',
            profileData.memberships || 'American Medical Association\nSpecialty Medical Association',
            doctorInfo.avatar
          );
          return of(mockProfile);
        } else {
          const mockProfile = new DoctorProfile(
            userUuid,
            'Unknown',
            'Doctor',
            'MD',
            'General Practice',
            5,
            'Doctor profile not found.',
            'unknown@example.com',
            '(000) 000-0000',
            { street: 'Unknown', city: 'Unknown', state: 'Unknown', postalCode: '00000', country: 'Unknown' },
            'Unknown',
            'Unknown',
            'Unknown',
            '/assets/images/male_avatar_default.png'
          );
          return of(mockProfile);
        }
      }),
      delay(1000)
    );
  }

  updateDoctorProfile(userUuid: string, profile: DoctorProfile): Observable<DoctorProfile> {
    // Simulate API call with delay
    profile.uuid = userUuid;
    return of(profile).pipe(delay(1500));
  }
}
