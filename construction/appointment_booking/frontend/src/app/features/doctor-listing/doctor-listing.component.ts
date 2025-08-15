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

  // Search and filter
  searchTerm = '';
  selectedSpecialty = '';
  sortBy = 'name';
  sortDir = 'asc';

  // Data
  doctors = signal<DoctorBookingInfoModels[]>([]);
  isLoading = signal(false);

  // Pagination
  currentPage = signal(0);
  pageSize = signal(6);
  totalElements = signal(0);
  totalPages = signal(0);
  hasNext = signal(false);
  hasPrevious = signal(false);

  // Specialties for filter dropdown
  specialties = [
    'General Practice',
    'Cardiology',
    'Dermatology',
    'Neurology',
    'Pediatrics',
    'Orthopedics',
    'Ophthalmology',
    'Psychiatry',
    'Dentistry'
  ];

  ngOnInit(): void {
    this.loadDoctors();
  }

  private loadDoctors(): void {
    this.isLoading.set(true);
    this.doctorBookingService.getAllDoctors(
      this.currentPage(),
      this.pageSize(),
      this.searchTerm,
      this.selectedSpecialty,
      this.sortBy,
      this.sortDir
    ).subscribe({
      next: (response) => {
        this.doctors.set(response.doctors);
        this.currentPage.set(response.page);
        this.totalElements.set(response.totalElements);
        this.totalPages.set(response.totalPages);
        this.hasNext.set(response.hasNext);
        this.hasPrevious.set(response.hasPrevious);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
      }
    });
  }

  filterDoctors() {
    this.currentPage.set(0); // Reset to first page
    this.loadDoctors();
  }

  onSpecialtyChange() {
    this.currentPage.set(0); // Reset to first page
    this.loadDoctors();
  }

  onSortChange() {
    this.currentPage.set(0); // Reset to first page
    this.loadDoctors();
  }

  bookAppointment(doctor: DoctorBookingInfoModels): void {
    this.router.navigate(['/appointment-booking'], {
      queryParams: { doctorID: doctor.doctorID }
    });
  }

  // Pagination methods
  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages()) {
      this.currentPage.set(page);
      this.loadDoctors();
    }
  }

  previousPage(): void {
    if (this.hasPrevious()) {
      this.goToPage(this.currentPage() - 1);
    }
  }

  nextPage(): void {
    if (this.hasNext()) {
      this.goToPage(this.currentPage() + 1);
    }
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const total = this.totalPages();
    const current = this.currentPage();
    
    // Show at most 5 page numbers
    let start = Math.max(0, current - 2);
    let end = Math.min(total - 1, current + 2);
    
    // Adjust if we don't have enough pages on one side
    if (end - start < 4) {
      if (start === 0) {
        end = Math.min(total - 1, start + 4);
      } else if (end === total - 1) {
        start = Math.max(0, end - 4);
      }
    }
    
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    
    return pages;
  }
}
