import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { DoctorGuard } from './core/guards/doctor.guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./features/login/login.component').then(m => m.LoginComponent) },
  { path: 'home', loadComponent: () => import('./features/home/home.component').then(m => m.HomeComponent) },
  {
    path: '',
    loadComponent: () => import('./layouts/shell/shell.component').then(m => m.ShellComponent),
    children: [
      { path: '', loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)},
      { path: 'doctors', loadComponent: () => import('./features/doctor-listing/doctor-listing.component').then(m => m.DoctorListingComponent), canActivate: [AuthGuard] },
      { path: 'appointment-booking', loadComponent: () => import('./features/appointment-booking/appointment-booking.component').then(m => m.AppointmentBookingComponent), canActivate: [AuthGuard] },
      { path: 'profile', loadComponent: () => import('./features/profile/profile.component').then(m => m.ProfileComponent), canActivate: [AuthGuard] },
      { path: 'doctor/profile', loadComponent: () => import('./features/doctor-profile/doctor-profile.component').then(m => m.DoctorProfileComponent), canActivate: [AuthGuard] }
    ]
  },
  { path: '**', redirectTo: '/login' }
];
