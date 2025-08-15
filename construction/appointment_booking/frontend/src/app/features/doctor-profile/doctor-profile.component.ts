// src/app/features/doctor-profile/doctor-profile.component.ts
import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { DoctorService } from '../../core/services/doctor.service';
import { DoctorProfile } from '../../shared/models/doctor.model';

@Component({
  selector: 'app-doctor-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './doctor-profile.component.html',
  styleUrls: ['./doctor-profile.component.scss']
})
export class DoctorProfileComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private authService = inject(AuthService);
  private doctorService = inject(DoctorService);

  isLoading = false;

  isReadonly = !this.canEdit();

  // Ảnh preview (mặc định dùng assets)
  previewUrl = signal<string>('/assets/images/male_avatar_default.png');
  private file: File | null = null;

  form = this.fb.group({
    firstName: ['', Validators.required],
    lastName:  ['', Validators.required],
    professionalTitle: [''],
    primarySpecialty:  ['', Validators.required],
    yearsExperience:   [0, [Validators.min(0)]],
    biography:         [''],

    email: ['', [Validators.required, Validators.email]],
    phone: ['', Validators.required],
    address: this.fb.group({
      street:      [''],
      city:        [''],
      state:       [''],
      postalCode:  [''],
      country:     [''],
    }),

    medicalLicenseNumber: [''],
    certifications: [''],
    memberships:   [''],
  });

  ngOnInit(): void {
    this.loadDoctorProfile();
  }

  private loadDoctorProfile(): void {
    const user = this.authService.getCurrentUser();
    const doctorID = this.route.snapshot.queryParams['doctorID'];
    console.log('------------------------------------------------')
    console.log(doctorID)
    let targetDoctorID: string;

    if (doctorID) {
      // Viewing another doctor's profile
      targetDoctorID = doctorID;
    } else {
      // No doctorID provided, check if current user is doctor
      if (!user?.uuid) return;

      if (user.userRole !== 'doctor') {
        alert('Access denied. Only doctors can view this page without specifying a doctor ID.');
        this.router.navigate(['/']);
        return;
      }

      targetDoctorID = user.uuid;

      // If user is doctor, populate form with current user data
      this.form.patchValue({
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        phone: user.phone
      });
    }

    this.isLoading = true;
    this.doctorService.getDoctorProfile(targetDoctorID).subscribe({
      next: (profile) => {
        this.form.patchValue(profile);
        if (profile.avatar) {
          this.previewUrl.set(profile.avatar);
        }
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        alert('Failed to load doctor profile.');
      }
    });
  }

  get fullName() {
    const v = this.form.value;
    return `Dr. ${v.firstName ?? ''} ${v.lastName ?? ''}`.trim();
  }

  onPickAvatar(e: Event) {
    const f = (e.target as HTMLInputElement).files?.[0];
    if (!f) return;
    this.file = f;
    this.previewUrl.set(URL.createObjectURL(f)); // preview tạm
  }

  public save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const user = this.authService.getCurrentUser();
    if (!user?.uuid) return;

    const formData = this.form.getRawValue();
    const doctorProfile = DoctorProfile.fromFormData(formData, user.uuid);
    doctorProfile.avatar = this.previewUrl();

    this.isLoading = true;
    this.doctorService.updateDoctorProfile(user.uuid, doctorProfile).subscribe({
      next: (updatedProfile) => {
        alert('Profile updated successfully!');
        this.isLoading = false;
      },
      error: () => {
        alert('Failed to update profile. Please try again.');
        this.isLoading = false;
      }
    });
  }

  cancel() {
    this.router.navigate(['health-connect/profile']); // hoặc history.back()
  }

  canEdit(): boolean {
    const user = this.authService.getCurrentUser();
    return user?.userRole === 'doctor';
  }

  onImageError(event: Event) {
    (event.target as HTMLImageElement).src = '/assets/images/male_avatar_default.png';
  }
}
