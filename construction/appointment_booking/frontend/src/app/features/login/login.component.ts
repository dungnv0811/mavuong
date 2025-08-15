import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { UserModel } from '../../shared/models/booking-docter-models';
import {DEFAULT_LOGO} from '../constants/common.constants';

declare const google: any;

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <img src="{{ DEFAULT_LOGO }}" alt="logo" height="40" />
        <h2 class="text-center mb-4">{{ isSignupMode ? 'Create Account' : 'Welcome to HealthConnect' }}</h2>

        <!-- Login/Signup Form -->
        <form (ngSubmit)="isSignupMode ? signup() : loginWithPassword()" class="mb-4">
          <div *ngIf="isSignupMode" class="row">
            <div class="col-6 mb-3">
              <input type="text" class="form-control" placeholder="First Name"
                     [(ngModel)]="firstName" name="firstName" required>
            </div>
            <div class="col-6 mb-3">
              <input type="text" class="form-control" placeholder="Last Name"
                     [(ngModel)]="lastName" name="lastName" required>
            </div>
          </div>
          <div *ngIf="!isSignupMode" class="mb-3">
            <input type="text" class="form-control" placeholder="Username or Email"
                   [(ngModel)]="username" name="username" required>
          </div>
          <div *ngIf="isSignupMode" class="mb-3">
            <input type="text" class="form-control" placeholder="Username"
                   [(ngModel)]="username" name="username" required>
          </div>
          <div *ngIf="isSignupMode" class="mb-3">
            <input type="email" class="form-control" placeholder="Email"
                   [(ngModel)]="email" name="email" required>
          </div>
          <div class="mb-3">
            <input type="password" class="form-control" placeholder="Password"
                   [(ngModel)]="password" name="password" required>
          </div>
          <div *ngIf="isSignupMode" class="mb-3">
            <input type="tel" class="form-control" placeholder="Phone"
                   [(ngModel)]="phone" name="phone" required>
          </div>
          <div *ngIf="isSignupMode" class="mb-3">
            <input type="date" class="form-control" placeholder="Date of Birth"
                   [(ngModel)]="dob" name="dob" required>
          </div>
          <div *ngIf="isSignupMode" class="mb-3">
            <input type="text" class="form-control" placeholder="Address"
                   [(ngModel)]="address" name="address" required>
          </div>
          <div *ngIf="isSignupMode" class="mb-3">
            <select class="form-select" [(ngModel)]="userRole" name="userRole" required>
              <option value="">Select Role</option>
              <option value="patient">Patient</option>
              <option value="doctor">Doctor</option>
            </select>
          </div>
          <button type="submit" class="btn btn-primary w-100" [disabled]="isLoading">
            {{ isLoading ? (isSignupMode ? 'Creating Account...' : 'Logging in...') : (isSignupMode ? 'Sign Up' : 'Login') }}
          </button>
        </form>

        <div class="text-center mb-3">
          <button type="button" class="btn btn-link" (click)="toggleMode()">
            {{ isSignupMode ? "Already have an account? Login" : "Don't have an account? Sign Up" }}
          </button>
        </div>

        <div class="divider">OR</div>

        <!-- Google Login -->
        <div class="text-center">
          <div id="g_id_onload"></div>
          <div id="g_id_signin" class="g_id_signin"></div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f8f9fa;
    }
    .login-card {
      background: white;
      padding: 2rem;
      border-radius: 8px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
      width: 100%;
      max-width: 400px;
    }
    .login-card img {
      display: block;
      margin: 0 auto 1rem auto;
    }
    .divider {
      text-align: center;
      margin: 1rem 0;
      position: relative;
      color: #666;
    }
    .divider::before {
      content: '';
      position: absolute;
      top: 50%;
      left: 0;
      right: 0;
      height: 1px;
      background: #ddd;
      z-index: 1;
    }
    .divider::after {
      content: 'OR';
      background: white;
      padding: 0 1rem;
      position: relative;
      z-index: 2;
    }
  `]
})
export class LoginComponent implements OnInit {
  username = '';
  email = '';
  password = '';
  firstName = '';
  lastName = '';
  phone = '';
  dob = '';
  address = '';
  userRole = '';
  isLoading = false;
  isSignupMode = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (typeof document !== 'undefined') {
      this.loadGoogleScript();
    }
  }

  private loadGoogleScript(): void {
    if (typeof google !== 'undefined') {
      this.initializeGoogleSignIn();
      return;
    }

    if (typeof document !== 'undefined') {
      const script = document.createElement('script');
      script.src = 'https://accounts.google.com/gsi/client';
      script.onload = () => this.initializeGoogleSignIn();
      document.head.appendChild(script);
    }
  }

  private initializeGoogleSignIn(): void {
    if (typeof document !== 'undefined') {
      google.accounts.id.initialize({
        client_id: '319532061520-ttbv4hqhbhkpiagl3upvatnnadksg1a0.apps.googleusercontent.com',
        callback: this.handleCredentialResponse.bind(this),
        auto_select: false,
        ux_mode: 'popup'
      });

      google.accounts.id.renderButton(
        document.getElementById('g_id_signin'),
        {
          theme: 'outline',
          size: 'large',
          width: 300
        }
      );
    }
  }

  loginWithPassword(): void {
    if (!this.username || !this.password) return;

    if (this.isLoading) {
      console.log('🔐 Login already in progress, ignoring duplicate request');
      return;
    }

    this.isLoading = true;
    console.log('🔐 Login component attempting login for:', this.username);
    this.authService.login(this.username, this.password).subscribe({
      next: (user) => {
        console.log('🔐 Login component successful, navigating to dashboard');
        this.router.navigate(['health-connect']);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('🔐 Login component error:', error);
        this.isLoading = false;
        alert(`Login failed: ${error.message || 'Please check your credentials.'}`);
      }
    });
  }

  signup(): void {
    if (!this.username || !this.email || !this.password || !this.firstName || !this.lastName || !this.phone || !this.dob || !this.address || !this.userRole) return;

    const userData = {
      username: this.username,
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      phone: this.phone,
      dob: this.dob,
      address: this.address,
      userRole: this.userRole
    };

    this.isLoading = true;
    this.authService.signup(this.password, userData).subscribe({
      next: (user) => {
        this.router.navigate(['']);
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        alert('Signup failed. Please try again.');
      }
    });
  }

  toggleMode(): void {
    this.isSignupMode = !this.isSignupMode;
    this.username = '';
    this.email = '';
    this.password = '';
    this.firstName = '';
    this.lastName = '';
    this.phone = '';
    this.dob = '';
    this.address = '';
    this.userRole = '';
  }

  handleCredentialResponse(response: any): void {
    const token = response.credential;
    const payload = JSON.parse(atob(token.split('.')[1]));

    const user = new UserModel(
      'user-' + payload.sub,
      payload.email.split('@')[0],
      payload.given_name || payload.name.split(' ')[0],
      payload.family_name || payload.name.split(' ')[1] || '',
      payload.email,
      '',
      '',
      '',
      'patient'
    );
    this.authService.loginWithGoogle(user);

  }

  protected readonly DEFAULT_LOGO = DEFAULT_LOGO;
}
