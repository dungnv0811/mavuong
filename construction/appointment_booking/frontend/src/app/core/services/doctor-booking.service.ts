import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { delay, map, catchError } from 'rxjs/operators';
import { DoctorBookingInfoModels } from '../../shared/models/booking-docter-models';
import { DEFAULT_AVATAR_MALE, DEFAULT_AVATAR_FEMALE } from '../../features/constants/common.constants';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DoctorBookingService {
  private http = inject(HttpClient);
  private baseUrl = environment.apiUrl;

  // Fallback mock data for offline mode
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
    console.log('🏥 Getting doctor booking info for:', doctorID);

    return this.http.get<any>(`${this.baseUrl}/doctors/${doctorID}/booking-info`).pipe(
      map((response: any) => {
        console.log('🏥 Backend doctor booking response:', response);
        return new DoctorBookingInfoModels(
          response.doctorID || doctorID,
          response.name,
          response.specialty,
          response.averageRating || response.rating,
          response.totalReviews || response.reviews,
          response.nextAvailable || 'Available Today',
          response.avatar || (response.name?.includes('Elena') || response.name?.includes('Emily') || response.name?.includes('Sarah') || response.name?.includes('Olivia') || response.name?.includes('Sofia') ? DEFAULT_AVATAR_FEMALE : DEFAULT_AVATAR_MALE)
        );
      }),
      catchError((error) => {
        console.warn('🏥 Backend call failed for doctor booking info, using mock data:', error);
        const doctor = this.mockDoctors.find(d => d.doctorID === doctorID);
        return of(doctor || null);
      })
    );
  }

  getAllDoctors(page: number = 0, size: number = 10, search: string = '', specialty: string = '', sortBy: string = 'name', sortDir: string = 'asc'): Observable<any> {
    console.log('🏥 Getting all doctors from backend with pagination');
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
      search: search,
      specialty: specialty,
      sortBy: sortBy,
      sortDir: sortDir
    });

    return this.http.get<any>(`${this.baseUrl}/doctors?${params.toString()}`).pipe(
      map((response: any) => {
        console.log('🏥 Backend paginated doctors response:', response);
        const doctors = response.doctors || response;

        const mappedDoctors = doctors.map((doctor: any) => new DoctorBookingInfoModels(
          doctor.doctorID || doctor.id || `doc-${doctor.id}`,
          doctor.name,
          doctor.specialty || doctor.specialties?.[0] || 'General Practice',
          doctor.averageRating || doctor.rating || 4.5,
          doctor.totalReviews || doctor.reviews || 50,
          doctor.nextAvailable || 'Available Today',
          doctor.avatar || (Math.random() > 0.5 ? DEFAULT_AVATAR_MALE : DEFAULT_AVATAR_FEMALE)
        ));

        return {
          doctors: mappedDoctors,
          page: response.page || 0,
          size: response.size || size,
          totalElements: response.totalElements || doctors.length,
          totalPages: response.totalPages || Math.ceil((response.totalElements || doctors.length) / size),
          hasNext: response.hasNext || false,
          hasPrevious: response.hasPrevious || false
        };
      }),
      catchError((error) => {
        console.warn('🏥 Backend call failed for doctors list, using mock data:', error);
        // Apply client-side pagination to mock data
        const filteredDoctors = this.mockDoctors.filter(doctor =>
          search === '' ||
          doctor.name.toLowerCase().includes(search.toLowerCase()) ||
          doctor.specialty.toLowerCase().includes(search.toLowerCase())
        );
        const paginatedDoctors = filteredDoctors.slice(page * size, (page + 1) * size);

        return of({
          doctors: paginatedDoctors,
          page: page,
          size: size,
          totalElements: filteredDoctors.length,
          totalPages: Math.ceil(filteredDoctors.length / size),
          hasNext: (page + 1) * size < filteredDoctors.length,
          hasPrevious: page > 0
        }).pipe(delay(500));
      })
    );
  }

  // Keep the original method for backward compatibility
  getAllDoctorsSimple(): Observable<DoctorBookingInfoModels[]> {
    return this.getAllDoctors().pipe(
      map(response => response.doctors)
    );
  }
}
