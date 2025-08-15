import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { AppointmentModel, UserModel, AppointmentStatus } from '../../shared/models/booking-docter-models';
import { DEFAULT_AVATAR_MALE, DEFAULT_AVATAR_FEMALE } from '../constants/common.constants';
import { AuthService } from '../../core/auth/auth.service';
import { AppointmentService } from '../../core/services/appointment.service';
import { ScheduleService } from '../../core/services/schedule.service';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private appointmentService = inject(AppointmentService);
  private scheduleService = inject(ScheduleService);
  private userService = inject(UserService);

  AppointmentStatus = AppointmentStatus;

  ngOnInit(): void {
    this.loadUserData();
    this.loadAppointmentHistory();
  }

  private loadAppointmentHistory(): void {
    const user = this.authService.getCurrentUser();
    if (user?.uuid) {
      // Use paginated method for better performance, but just get the first page
      this.appointmentService.getAppointmentHistoryPaginated(user.uuid, 0, 20).subscribe({
        next: (response) => {
          this.history.set(response.appointments);
        },
        error: () => {
          this.history.set([]);
        }
      });
    }
  }

  private loadUserData(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.form.patchValue({
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        phone: user.phone,
        dob: user.dob,
        address: user.address
      });
    }
  }

  avatarUrl = signal('assets/avatars/me.jpg'); // đổi thành ảnh của bạn

  form = this.fb.group({
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required]],
    dob: ['', [Validators.required]],
    address: ['']
  });

  history = signal<AppointmentModel[]>([]);

  // Settings
  emailNotifications = signal(true);
  privacyMode = signal(false);
  language = signal('English');

  password = this.fb.control('');

  saveProfile() {
    if (this.form.invalid) { 
      this.form.markAllAsTouched(); 
      return; 
    }
    
    const user = this.authService.getCurrentUser();
    if (!user?.uuid) {
      alert('User not logged in!');
      return;
    }
    
    const formData = this.form.getRawValue();
    const profileData = {
      username: user.username,
      firstName: formData.firstName!,
      lastName: formData.lastName!,
      email: formData.email!,
      phone: formData.phone!,
      dob: formData.dob!,
      address: formData.address!
    };
    
    this.userService.updateUserProfile(user.uuid, profileData).subscribe({
      next: (updatedUser) => {
        if (updatedUser) {
          // Update the current user in auth service
          this.authService.setCurrentUser(updatedUser);
          alert('Profile saved successfully!');
        } else {
          alert('Failed to save profile. Please try again.');
        }
      },
      error: () => {
        alert('Failed to save profile. Please try again.');
      }
    });
  }

  view(item: AppointmentModel) { alert('View appointment: ' + item.doctor); }
  reschedule(item: AppointmentModel) { alert('Reschedule: ' + item.doctor); }

  cancel(item: AppointmentModel) {
    if (confirm('Are you sure you want to cancel this appointment?')) {
      // Cancel appointment
      this.appointmentService.cancelAppointment(item.uuid).subscribe({
        next: (cancelledAppointment) => {
          if (cancelledAppointment) {
            // Parse date and time from dateTime string
            const dateTimeStr = item.dateTime; // e.g., "2024-12-20 at 10:00 AM"
            const [dateStr, , timeStr] = dateTimeStr.split(' ');

            // Release the time slot
            this.scheduleService.releaseTimeSlot(item.doctorID, dateStr, timeStr).subscribe({
              next: () => {
                // Refresh appointment history
                this.loadAppointmentHistory();
                alert('Appointment cancelled successfully!');
              },
              error: () => {
                // Still refresh history even if time slot release fails
                this.loadAppointmentHistory();
                alert('Appointment cancelled, but time slot may still appear booked.');
              }
            });
          } else {
            alert('Failed to cancel appointment.');
          }
        },
        error: () => {
          alert('Failed to cancel appointment. Please try again.');
        }
      });
    }
  }

  toggleEmail(event: Event) {
    const input = event.target as HTMLInputElement;
    this.emailNotifications.set(input.checked);
  }
  togglePrivacy(event: Event) {
    const input = event.target as HTMLInputElement;
    this.privacyMode.set(input.checked);
  }
  changeLanguage(v: string) { this.language.set(v); }
  updatePassword() {
    if (!this.password.value) return;
    alert('Password updated (demo).');
    this.password.reset('');
  }
}
