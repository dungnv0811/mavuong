import { Component, computed, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { DoctorBookingService } from '../../core/services/doctor-booking.service';
import { ScheduleService } from '../../core/services/schedule.service';
import { AuthService } from '../../core/auth/auth.service';
import { AppointmentService } from '../../core/services/appointment.service';
import { TimeSlot } from '../../shared/models/schedule.model';
import { AppointmentModel, AppointmentStatus } from '../../shared/models/booking-docter-models';

@Component({
  selector: 'app-appointment-booking',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './appointment-booking.component.html',
  styleUrls: ['./appointment-booking.component.scss']
})
export class AppointmentBookingComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private doctorBookingService = inject(DoctorBookingService);
  private scheduleService = inject(ScheduleService);
  private authService = inject(AuthService);
  private appointmentService = inject(AppointmentService);

  doctor = signal({
    doctorID: '',
    name: 'Dr. Emily Chen',
    specialty: 'Pediatrician',
    rating: 4.9,
    reviews: 230,
    avatar: 'assets/avatars/2.jpg',
  });
  
  isLoading = signal(false);

  ngOnInit(): void {
    this.loadDoctorInfo();
    this.loadPatientInfo();
  }

  private loadPatientInfo(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.form.patchValue({
        fullName: `${user.firstName} ${user.lastName}`.trim(),
        email: user.email,
        phone: user.phone
      });
    }
  }

  private loadDoctorInfo(): void {
    const doctorID = this.route.snapshot.queryParams['doctorID'];
    if (!doctorID) return;

    this.isLoading.set(true);
    this.doctorBookingService.getDoctorBookingInfo(doctorID).subscribe({
      next: (doctorInfo) => {
        if (doctorInfo) {
          this.doctor.set({
            doctorID: doctorInfo.doctorID,
            name: doctorInfo.name,
            specialty: doctorInfo.specialty,
            rating: doctorInfo.rating,
            reviews: doctorInfo.reviews,
            avatar: doctorInfo.avatar
          });
        }
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
      }
    });
  }

  // time slots
  date = signal<string>('');
  slots = signal<TimeSlot[]>([]);
  chosenSlot = signal<string>('');

  form = this.fb.group({
    fullName: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required]],
    gender: [''],
    reason: [''],
    insurance: [''],
    policyNumber: [''],
    agree: [false, [Validators.requiredTrue]],
  });

  canSubmit = computed(() => !!this.date() && !!this.chosenSlot() && this.form.valid);

  pickDate(e: Event) {
    const input = e.target as HTMLInputElement;
    const selectedDate = input.value;
    this.date.set(selectedDate);
    
    if (selectedDate && this.doctor().doctorID) {
      this.loadTimeSlots(this.doctor().doctorID, selectedDate);
    }
  }
  
  pickSlot(v: string) { this.chosenSlot.set(v); }
  
  private loadTimeSlots(doctorID: string, date: string): void {
    this.scheduleService.getDoctorSchedule(doctorID, date).subscribe({
      next: (schedule) => {
        this.slots.set(schedule.timeSlots);
      },
      error: () => {
        this.slots.set([]);
      }
    });
  }

  viewDoctorProfile(): void {
    if (this.doctor().doctorID) {
      this.router.navigate(['/doctor/profile'], {
        queryParams: { doctorID: this.doctor().doctorID }
      });
    }
  }

  submit() {
    if (!this.canSubmit()) { this.form.markAllAsTouched(); return; }
    
    const user = this.authService.getCurrentUser();
    if (!user) return;

    const formData = this.form.getRawValue();
    const doctorInfo = this.doctor();
    
    // Create new appointment
    const newAppointment = new AppointmentModel(
      'apt-' + Date.now(),
      doctorInfo.doctorID,
      user.uuid,
      doctorInfo.name,
      doctorInfo.specialty,
      `${this.date()} at ${this.chosenSlot()}`,
      'Medical Center', // Default location
      AppointmentStatus.UPCOMING,
      doctorInfo.avatar
    );

    this.isLoading.set(true);
    
    // Book the time slot first
    this.scheduleService.bookTimeSlot(doctorInfo.doctorID, this.date(), this.chosenSlot()).subscribe({
      next: (success) => {
        if (success) {
          // Create appointment
          this.appointmentService.createAppointment(newAppointment).subscribe({
            next: (appointment) => {
              alert('Appointment booked successfully!');
              this.router.navigate(['/']);
              this.isLoading.set(false);
            },
            error: () => {
              alert('Failed to create appointment. Please try again.');
              this.isLoading.set(false);
            }
          });
        } else {
          alert('Time slot is no longer available. Please choose another time.');
          this.loadTimeSlots(doctorInfo.doctorID, this.date()); // Refresh slots
          this.isLoading.set(false);
        }
      },
      error: () => {
        alert('Failed to book time slot. Please try again.');
        this.isLoading.set(false);
      }
    });
  }
}
