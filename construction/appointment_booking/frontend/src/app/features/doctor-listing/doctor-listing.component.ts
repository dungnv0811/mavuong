// src/app/features/octor-listidng/doctor-listing.component.ts
import { Component, signal, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorBookingInfoModels } from '../../shared/models/booking-docter-models';
import { DoctorBookingService } from '../../core/services/doctor-booking.service';

@Component({
  selector: 'app-doctor-listing',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './doctor-listing.component.html',
  styleUrls: ['./doctor-listing.component.scss']
})
export class DoctorListingComponent implements OnInit {
  private doctorBookingService = inject(DoctorBookingService);
  private router = inject(Router);

  searchTerm = '';
  doctors = signal<DoctorBookingInfoModels[]>([]);
  filteredDoctors = signal<DoctorBookingInfoModels[]>([]);
  isLoading = signal(false);

  ngOnInit(): void {
    this.loadDoctors();
  }

  private loadDoctors(): void {
    this.isLoading.set(true);
    this.doctorBookingService.getAllDoctors().subscribe({
      next: (doctors) => {
        this.doctors.set(doctors);
        this.filteredDoctors.set(doctors);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
      }
    });
  }

  filterDoctors() {
    const filtered = this.doctors().filter(doctor =>
      doctor.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      doctor.specialty.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
    this.filteredDoctors.set(filtered);
  }

  bookAppointment(doctor: DoctorBookingInfoModels): void {
    this.router.navigate(['/appointment-booking'], {
      queryParams: { doctorID: doctor.doctorID }
    });
  }

  sortBy(field: keyof DoctorBookingInfoModels) {
    const sorted = [...this.filteredDoctors()].sort((a, b) => {
      if (field === 'rating') {
        return b[field] - a[field];
      }
      return a[field].toString().localeCompare(b[field].toString());
    });
    this.filteredDoctors.set(sorted);
  }
}
