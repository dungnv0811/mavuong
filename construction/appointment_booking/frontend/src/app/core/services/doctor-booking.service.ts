import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { DoctorBookingInfoModels } from '../../shared/models/booking-docter-models';
import { DEFAULT_AVATAR_MALE, DEFAULT_AVATAR_FEMALE } from '../../features/constants/common.constants';

@Injectable({
  providedIn: 'root'
})
export class DoctorBookingService {
  
  private mockDoctors: DoctorBookingInfoModels[] = [
    new DoctorBookingInfoModels('doc-001', 'Dr. Elena Rodriguez', 'General Practitioner', 4.8, 123, 'Today, 3:00 PM', DEFAULT_AVATAR_FEMALE),
    new DoctorBookingInfoModels('doc-002', 'Dr. Michael Chen', 'Pediatrician', 4.9, 98, 'Tomorrow, 10:00 AM', DEFAULT_AVATAR_MALE),
    new DoctorBookingInfoModels('doc-003', 'Dr. Sarah Kim', 'Dermatologist', 4.7, 150, 'Mon, Sep 18', DEFAULT_AVATAR_FEMALE),
    new DoctorBookingInfoModels('doc-004', 'Dr. David Green', 'Cardiologist', 4.6, 110, 'Wed, Sep 20', DEFAULT_AVATAR_MALE),
    new DoctorBookingInfoModels('doc-005', 'Dr. Emily White', 'Ophthalmologist', 4.9, 200, 'Today, 4:30 PM', DEFAULT_AVATAR_FEMALE),
    new DoctorBookingInfoModels('doc-006', 'Dr. James Brown', 'Orthopedic Surgeon', 4.3, 70, 'Fri, Sep 22', DEFAULT_AVATAR_MALE),
    new DoctorBookingInfoModels('doc-007', 'Dr. Olivia Perez', 'Neurologist', 4.7, 110, 'Tomorrow, 11:00 AM', DEFAULT_AVATAR_FEMALE),
    new DoctorBookingInfoModels('doc-008', 'Dr. Ben Carter', 'Psychiatrist', 4.8, 90, 'Mon, Sep 25', DEFAULT_AVATAR_MALE),
    new DoctorBookingInfoModels('doc-009', 'Dr. Sofia Garcia', 'Dentist', 4.5, 180, 'Today, 2:00 PM', DEFAULT_AVATAR_FEMALE),
  ];

  getDoctorBookingInfo(doctorID: string): Observable<DoctorBookingInfoModels | null> {
    const doctor = this.mockDoctors.find(d => d.doctorID === doctorID);
    return of(doctor || null).pipe(delay(500));
  }

  getAllDoctors(): Observable<DoctorBookingInfoModels[]> {
    return of(this.mockDoctors).pipe(delay(500));
  }
}