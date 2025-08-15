import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { delay, map, catchError } from 'rxjs/operators';
import { AppointmentModel, AppointmentStatus } from '../../shared/models/booking-docter-models';
import { DEFAULT_AVATAR_MALE, DEFAULT_AVATAR_FEMALE } from '../../features/constants/common.constants';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {
  private http = inject(HttpClient);
  private baseUrl = environment.apiUrl;
  
  // Fallback mock data for offline mode
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
    console.log('📅 Getting upcoming appointments for user:', userID);
    
    return this.http.get<any>(`${this.baseUrl}/appointments/upcoming?userID=${userID}`).pipe(
      map((response: any) => {
        console.log('📅 Backend upcoming appointments response:', response);
        const appointments = response.appointments || response;
        
        return appointments.map((apt: any) => new AppointmentModel(
          apt.uuid || apt.id,
          apt.doctorID || apt.doctorId,
          apt.userID || apt.patientId || userID,
          apt.doctor || apt.doctorName || `Doctor ${apt.doctorId}`,
          apt.specialty || apt.doctorSpecialty || 'General Practice',
          apt.dateTime || apt.appointmentDateTime,
          apt.place || apt.location || 'Hospital',
          this.mapStatus(apt.status),
          apt.avatarLink || (apt.doctor?.includes('Elena') || apt.doctor?.includes('Emily') || apt.doctor?.includes('Sarah') || apt.doctor?.includes('Olivia') || apt.doctor?.includes('Sofia') ? DEFAULT_AVATAR_FEMALE : DEFAULT_AVATAR_MALE)
        ));
      }),
      catchError((error) => {
        console.warn('📅 Backend call failed for upcoming appointments, using mock data:', error);
        const upcoming = this.mockAppointments.filter(apt => 
          apt.userID === userID && apt.status === AppointmentStatus.UPCOMING
        );
        return of(upcoming).pipe(delay(500));
      })
    );
  }

  getAppointmentHistory(userID: string): Observable<AppointmentModel[]> {
    console.log('📅 Getting appointment history for user:', userID);
    
    return this.http.get<any>(`${this.baseUrl}/appointments/history?userID=${userID}`).pipe(
      map((response: any) => {
        console.log('📅 Backend appointment history response:', response);
        const appointments = response.appointments || response;
        
        return appointments.map((apt: any) => new AppointmentModel(
          apt.uuid || apt.id,
          apt.doctorID || apt.doctorId,
          apt.userID || apt.patientId || userID,
          apt.doctor || apt.doctorName || `Doctor ${apt.doctorId}`,
          apt.specialty || apt.doctorSpecialty || 'General Practice',
          apt.dateTime || apt.appointmentDateTime,
          apt.place || apt.location || 'Hospital',
          this.mapStatus(apt.status),
          apt.avatarLink || (apt.doctor?.includes('Elena') || apt.doctor?.includes('Emily') || apt.doctor?.includes('Sarah') || apt.doctor?.includes('Olivia') || apt.doctor?.includes('Sofia') ? DEFAULT_AVATAR_FEMALE : DEFAULT_AVATAR_MALE)
        ));
      }),
      catchError((error) => {
        console.warn('📅 Backend call failed for appointment history, using mock data:', error);
        const history = this.mockAppointments.filter(apt => 
          apt.userID === userID && (apt.status === AppointmentStatus.COMPLETED || apt.status === AppointmentStatus.CANCELLED)
        );
        return of(history).pipe(delay(500));
      })
    );
  }

  getAllAppointments(userID: string): Observable<AppointmentModel[]> {
    const userAppointments = this.mockAppointments.filter(apt => apt.userID === userID);
    return of(userAppointments).pipe(delay(500));
  }

  // Paginated versions of appointment methods
  getUpcomingAppointmentsPaginated(userID: string, page: number = 0, size: number = 10): Observable<any> {
    console.log('📅 Getting upcoming appointments for user with pagination:', userID);
    
    return this.http.get<any>(`${this.baseUrl}/appointments/upcoming?userID=${userID}&page=${page}&size=${size}`).pipe(
      map((response: any) => {
        console.log('📅 Backend paginated upcoming appointments response:', response);
        const appointments = response.appointments || response;
        const mappedAppointments = appointments.map((apt: any) => new AppointmentModel(
          apt.uuid || apt.id,
          apt.doctorID || apt.doctorId,
          apt.userID || apt.patientId || userID,
          apt.doctor || apt.doctorName || `Doctor ${apt.doctorId}`,
          apt.specialty || apt.doctorSpecialty || 'General Practice',
          apt.dateTime || apt.appointmentDateTime,
          apt.place || apt.location || 'Hospital',
          this.mapStatus(apt.status),
          apt.avatarLink || (apt.doctor?.includes('Elena') || apt.doctor?.includes('Emily') || apt.doctor?.includes('Sarah') || apt.doctor?.includes('Olivia') || apt.doctor?.includes('Sofia') ? DEFAULT_AVATAR_FEMALE : DEFAULT_AVATAR_MALE)
        ));
        
        return {
          appointments: mappedAppointments,
          page: response.page || 0,
          size: response.size || size,
          totalElements: response.totalElements || appointments.length,
          totalPages: response.totalPages || Math.ceil((response.totalElements || appointments.length) / size),
          hasNext: response.hasNext || false,
          hasPrevious: response.hasPrevious || false
        };
      }),
      catchError((error) => {
        console.warn('📅 Backend call failed for upcoming appointments, using mock data:', error);
        const mockAppointments = this.mockAppointments.filter(apt => 
          apt.userID === userID && apt.status === AppointmentStatus.UPCOMING
        );
        const paginatedAppointments = mockAppointments.slice(page * size, (page + 1) * size);
        
        return of({
          appointments: paginatedAppointments,
          page: page,
          size: size,
          totalElements: mockAppointments.length,
          totalPages: Math.ceil(mockAppointments.length / size),
          hasNext: (page + 1) * size < mockAppointments.length,
          hasPrevious: page > 0
        }).pipe(delay(500));
      })
    );
  }

  getAppointmentHistoryPaginated(userID: string, page: number = 0, size: number = 10): Observable<any> {
    console.log('📅 Getting appointment history for user with pagination:', userID);
    
    return this.http.get<any>(`${this.baseUrl}/appointments/history?userID=${userID}&page=${page}&size=${size}`).pipe(
      map((response: any) => {
        console.log('📅 Backend paginated appointment history response:', response);
        const appointments = response.appointments || response;
        const mappedAppointments = appointments.map((apt: any) => new AppointmentModel(
          apt.uuid || apt.id,
          apt.doctorID || apt.doctorId,
          apt.userID || apt.patientId || userID,
          apt.doctor || apt.doctorName || `Doctor ${apt.doctorId}`,
          apt.specialty || apt.doctorSpecialty || 'General Practice',
          apt.dateTime || apt.appointmentDateTime,
          apt.place || apt.location || 'Hospital',
          this.mapStatus(apt.status),
          apt.avatarLink || (apt.doctor?.includes('Elena') || apt.doctor?.includes('Emily') || apt.doctor?.includes('Sarah') || apt.doctor?.includes('Olivia') || apt.doctor?.includes('Sofia') ? DEFAULT_AVATAR_FEMALE : DEFAULT_AVATAR_MALE)
        ));
        
        return {
          appointments: mappedAppointments,
          page: response.page || 0,
          size: response.size || size,
          totalElements: response.totalElements || appointments.length,
          totalPages: response.totalPages || Math.ceil((response.totalElements || appointments.length) / size),
          hasNext: response.hasNext || false,
          hasPrevious: response.hasPrevious || false
        };
      }),
      catchError((error) => {
        console.warn('📅 Backend call failed for appointment history, using mock data:', error);
        const mockAppointments = this.mockAppointments.filter(apt => 
          apt.userID === userID && (apt.status === AppointmentStatus.COMPLETED || apt.status === AppointmentStatus.CANCELLED)
        );
        const paginatedAppointments = mockAppointments.slice(page * size, (page + 1) * size);
        
        return of({
          appointments: paginatedAppointments,
          page: page,
          size: size,
          totalElements: mockAppointments.length,
          totalPages: Math.ceil(mockAppointments.length / size),
          hasNext: (page + 1) * size < mockAppointments.length,
          hasPrevious: page > 0
        }).pipe(delay(500));
      })
    );
  }

  createAppointment(appointment: AppointmentModel): Observable<AppointmentModel> {
    // Create appointment via backend
    const appointmentData = {
      doctorID: appointment.doctorID,
      userID: appointment.userID,
      dateTime: appointment.dateTime,
      place: appointment.place,
      patientInfo: {
        fullName: appointment.doctor, // This should be patient name in real implementation
        email: 'patient@example.com',
        phone: '+1234567890',
        gender: 'Other',
        reason: appointment.specialty,
        insurance: 'Basic',
        policyNumber: 'POL123456'
      }
    };

    return this.http.post(`${this.baseUrl}/appointments`, appointmentData).pipe(
      map((response: any) => new AppointmentModel(
        response.appointment.uuid,
        response.appointment.doctorID,
        response.appointment.userID,
        response.appointment.doctor,
        response.appointment.specialty,
        response.appointment.dateTime,
        response.appointment.place,
        this.mapStatus(response.appointment.status),
        response.appointment.avatarLink || DEFAULT_AVATAR_MALE
      )),
      catchError((error) => {
        console.warn('Backend call failed for create appointment, using mock simulation:', error);
        this.mockAppointments.push(appointment);
        return of(appointment).pipe(delay(1000));
      })
    );
  }

  cancelAppointment(appointmentId: string): Observable<AppointmentModel | null> {
    return this.http.put(`${this.baseUrl}/appointments/${appointmentId}/cancel`, {}).pipe(
      map((response: any) => new AppointmentModel(
        response.appointment.uuid,
        response.appointment.doctorID,
        response.appointment.userID,
        response.appointment.doctor,
        response.appointment.specialty,
        response.appointment.dateTime,
        response.appointment.place,
        this.mapStatus(response.appointment.status),
        response.appointment.avatarLink || DEFAULT_AVATAR_MALE
      )),
      catchError((error) => {
        console.warn('Backend call failed for cancel appointment, using mock simulation:', error);
        const appointment = this.mockAppointments.find(apt => apt.uuid === appointmentId);
        if (appointment) {
          appointment.status = AppointmentStatus.CANCELLED;
          return of(appointment).pipe(delay(1000));
        }
        return of(null).pipe(delay(1000));
      })
    );
  }

  private mapStatus(status: string): AppointmentStatus {
    switch (status?.toUpperCase()) {
      case 'UPCOMING': case 'CONFIRMED': case 'PENDING': return AppointmentStatus.UPCOMING;
      case 'COMPLETED': return AppointmentStatus.COMPLETED;
      case 'CANCELLED': return AppointmentStatus.CANCELLED;
      case 'BOOKED': return AppointmentStatus.BOOKED;
      default: return AppointmentStatus.UPCOMING;
    }
  }
}