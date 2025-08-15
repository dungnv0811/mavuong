import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { AppointmentModel, AppointmentStatus } from '../../shared/models/booking-docter-models';
import { DEFAULT_AVATAR_MALE, DEFAULT_AVATAR_FEMALE } from '../../features/constants/common.constants';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  
  private mockAppointments: AppointmentModel[] = [
    new AppointmentModel('apt-001', 'doc-001', 'user-001', 'Dr. Elena Rodriguez', 'General Practitioner', 'Oct 26, 2024 at 10:00 AM', 'HealthLink Clinic, Room 301', AppointmentStatus.UPCOMING, DEFAULT_AVATAR_FEMALE),
    new AppointmentModel('apt-002', 'doc-002', 'user-001', 'Dr. Michael Chen', 'Pediatrician', 'Nov 05, 2024 at 02:30 PM', 'City Hospital, Pediatrics Dept.', AppointmentStatus.UPCOMING, DEFAULT_AVATAR_MALE),
    new AppointmentModel('apt-003', 'doc-003', 'user-001', 'Dr. Sarah Kim', 'Dermatologist', 'Nov 12, 2024 at 11:00 AM', 'Family Care Clinic, Suite 2B', AppointmentStatus.UPCOMING, DEFAULT_AVATAR_FEMALE),
    new AppointmentModel('apt-101', 'doc-005', 'user-001', 'Dr. Emily White', 'Ophthalmologist', '2024-07-20 at 10:00 AM', 'Health Center', AppointmentStatus.COMPLETED, DEFAULT_AVATAR_FEMALE),
    new AppointmentModel('apt-102', 'doc-004', 'user-001', 'Dr. David Green', 'Cardiologist', '2024-06-13 at 02:30 PM', 'Medical Plaza', AppointmentStatus.COMPLETED, DEFAULT_AVATAR_MALE),
    new AppointmentModel('apt-103', 'doc-003', 'user-001', 'Dr. Sarah Kim', 'Dermatologist', '2024-05-15 at 11:00 AM', 'Wellness Clinic', AppointmentStatus.CANCELLED, DEFAULT_AVATAR_FEMALE),
    new AppointmentModel('apt-104', 'doc-006', 'user-001', 'Dr. James Brown', 'Orthopedic Surgeon', '2024-04-22 at 04:00 PM', 'Sports Medicine Center', AppointmentStatus.COMPLETED, DEFAULT_AVATAR_MALE),
  ];

  getUpcomingAppointments(userID: string): Observable<AppointmentModel[]> {
    const upcoming = this.mockAppointments.filter(apt => 
      apt.userID === userID && apt.status === AppointmentStatus.UPCOMING
    );
    return of(upcoming).pipe(delay(500));
  }

  getAppointmentHistory(userID: string): Observable<AppointmentModel[]> {
    const history = this.mockAppointments.filter(apt => 
      apt.userID === userID && (apt.status === AppointmentStatus.COMPLETED || apt.status === AppointmentStatus.CANCELLED || apt.status === AppointmentStatus.UPCOMING)
    );
    return of(history).pipe(delay(500));
  }

  getAllAppointments(userID: string): Observable<AppointmentModel[]> {
    const userAppointments = this.mockAppointments.filter(apt => apt.userID === userID);
    return of(userAppointments).pipe(delay(500));
  }

  createAppointment(appointment: AppointmentModel): Observable<AppointmentModel> {
    // Simulate creating new appointment
    this.mockAppointments.push(appointment);
    return of(appointment).pipe(delay(1000));
  }

  cancelAppointment(appointmentId: string): Observable<AppointmentModel | null> {
    const appointment = this.mockAppointments.find(apt => apt.uuid === appointmentId);
    if (appointment) {
      appointment.status = AppointmentStatus.CANCELLED;
      return of(appointment).pipe(delay(1000));
    }
    return of(null).pipe(delay(1000));
  }
}