import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private apiUrl = 'http://localhost:8080/api';
  
  symptomDescription = '';
  suggestedSpecialties: string[] = [];
  selectedSpecialty = '';
  doctors: any[] = [];
  selectedDoctor: any = null;
  appointmentDateTime = '';
  appointmentNotes = '';
  patientId = 1;
  appointments: any[] = [];
  
  constructor(private http: HttpClient) {
    this.loadDoctors();
  }
  
  analyzeSymptoms() {
    this.http.post(`${this.apiUrl}/symptoms/analyze`, {
      patientId: this.patientId,
      description: this.symptomDescription
    }).subscribe((response: any) => {
      this.suggestedSpecialties = response.suggestedSpecialties;
    });
  }
  
  loadDoctors() {
    const url = this.selectedSpecialty ? 
      `${this.apiUrl}/doctors?specialty=${this.selectedSpecialty}` : 
      `${this.apiUrl}/doctors`;
    
    this.http.get<any[]>(url).subscribe(doctors => {
      this.doctors = doctors;
    });
  }
  
  selectDoctor(doctor: any) {
    this.selectedDoctor = doctor;
  }
  
  bookAppointment() {
    this.http.post(`${this.apiUrl}/appointments`, {
      patientId: this.patientId,
      doctorId: this.selectedDoctor.id,
      appointmentDateTime: this.appointmentDateTime,
      notes: this.appointmentNotes
    }).subscribe(() => {
      alert('Appointment booked successfully!');
      this.selectedDoctor = null;
      this.appointmentDateTime = '';
      this.appointmentNotes = '';
    });
  }
  
  loadAppointments() {
    this.http.get<any[]>(`${this.apiUrl}/appointments?patientId=${this.patientId}`)
      .subscribe(appointments => {
        this.appointments = appointments;
      });
  }
}