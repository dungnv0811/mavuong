import { Component, computed, signal, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppointmentModel } from '../../shared/models/booking-docter-models';
import {RouterLink, Router} from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { AppointmentService } from '../../core/services/appointment.service';



@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);
  private appointmentService = inject(AppointmentService);

  upcoming = signal<AppointmentModel[]>([]);
  isLoading = signal(false);

  ngOnInit(): void {
    this.loadUpcomingAppointments();
  }

  private loadUpcomingAppointments(): void {
    const user = this.authService.getCurrentUser();
    if (!user?.uuid) return;

    this.isLoading.set(true);
    // Use paginated method but just get the first 5 appointments for dashboard
    this.appointmentService.getUpcomingAppointmentsPaginated(user.uuid, 0, 5).subscribe({
      next: (response) => {
        this.upcoming.set(response.appointments);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
      }
    });
  }

  hasUpcoming = computed(() => this.upcoming().length > 0);
  isAuthenticated = computed(() => this.authService.isAuthenticated());

  getCurrentUserName(): string {
    const user = this.authService.getCurrentUser();
    return user ? `${user.firstName} ${user.lastName}`.trim() : 'Guest';
  }

  viewDetails(appointment: AppointmentModel): void {
    this.router.navigate(['/doctor/profile'], {
      queryParams: { doctorID: appointment.doctorID }
    });
  }

  bookNew() { alert('Book new appointment – demo'); }
  viewHistory() { alert('Open appointment history – demo'); }

}
