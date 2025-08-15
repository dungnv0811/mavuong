import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserModel, AppointmentModel } from '../../shared/models/booking-docter-models';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // Doctor endpoints
  getDoctors(filters?: any): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/doctors`, { params: filters });
  }

  getDoctorById(id: string): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/doctors/${id}`);
  }

  getDoctorAvailability(id: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/doctors/${id}/availability`);
  }

  // Appointment endpoints
  bookAppointment(appointment: Partial<AppointmentModel>): Observable<AppointmentModel> {
    return this.http.post<AppointmentModel>(`${this.baseUrl}/appointments`, appointment);
  }

  getAppointments(): Observable<AppointmentModel[]> {
    return this.http.get<AppointmentModel[]>(`${this.baseUrl}/appointments`);
  }

  getAppointmentById(id: string): Observable<AppointmentModel> {
    return this.http.get<AppointmentModel>(`${this.baseUrl}/appointments/${id}`);
  }

  rescheduleAppointment(id: string, appointment: Partial<AppointmentModel>): Observable<AppointmentModel> {
    return this.http.put<AppointmentModel>(`${this.baseUrl}/appointments/${id}`, appointment);
  }

  cancelAppointment(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/appointments/${id}`);
  }

  // Symptom analysis endpoints
  analyzeSymptoms(description: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/symptoms/analyze`, { description });
  }
}