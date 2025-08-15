import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';
import { UserModel } from '../../shared/models/booking-docter-models';
import { environment } from '../../../environments/environment';
import {Router} from '@angular/router';

interface AuthResponse {
  user: {
    uuid: string;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    dob: string;
    address: string;
    userRole: string;
  };
  token: string;
}

declare const google: any;

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private baseUrl = environment.apiUrl;
  private currentUserSubject = new BehaviorSubject<UserModel | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private router: Router
  ) {
    if (typeof window !== 'undefined') {
      const savedUser = localStorage.getItem('user');
      if (savedUser) {
        this.currentUserSubject.next(JSON.parse(savedUser));
      }
    }
  }

  login(email: string, password: string): Observable<UserModel> {
    const loginData = { emailOrUsername: email, password: password };

    console.log('🔐 Attempting backend login with URL:', `${this.baseUrl}/auth/login`);
    console.log('🔐 Login payload:', loginData);

    return this.http.post<any>(`http://localhost:8090/api/v1/auth/login`, loginData).pipe(
      tap(response => console.log('🔐 Backend login response:', response)),
      map((response: any) => {
        if (!response || !response.user) {
          throw new Error('Invalid response from server');
        }

        console.log('🔐 Raw backend response:', response);

        const user = new UserModel(
          response.user.uuid,
          response.user.username,
          response.user.firstName,
          response.user.lastName,
          response.user.email,
          response.user.phone,
          response.user.dob,
          response.user.address,
          response.user.userRole?.toLowerCase() || response.user.userRole // Handle enum to string conversion
        );

        // Store user and token
        this.currentUserSubject.next(user);
        if (typeof window !== 'undefined') {
          localStorage.setItem('user', JSON.stringify(user));
          localStorage.setItem('token', response.token);
        }

        console.log('🔐 Backend login successful, user:', user);
        return user;
      }),
      catchError((error) => {
        console.error('🔐 Backend authentication failed:', error);
        console.error('🔐 Error details:', {
          status: error.status,
          statusText: error.statusText,
          message: error.message,
          error: error.error
        });

        // Provide specific error messages based on HTTP status
        let errorMessage = 'Login failed. Please try again.';
        if (error.status === 401 || error.status === 400) {
          errorMessage = 'Invalid username or password.';
        } else if (error.status === 0) {
          errorMessage = 'Cannot connect to server. Please check if the backend is running.';
        } else if (error.status >= 500) {
          errorMessage = 'Server error. Please try again later.';
        } else if (error.status === 200 && error.statusText === 'OK') {
          errorMessage = 'Server response format error. Please check backend configuration.';
        }

        throw new Error(errorMessage);
      })
    );
  }

  loginWithGoogle(user: UserModel): void {
    // Send Google user to backend for authentication/registration
    const googleLoginData = {
      username: user.username,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      phone: user.phone,
      dob: user.dob,
      address: user.address,
      userRole: user.userRole
    };

    console.log('🔐 Attempting Google login with backend');
    this.http.post<any>(`${this.baseUrl}/auth/google`, googleLoginData).pipe(
      map((response: any) => {
        const backendUser = new UserModel(
          'google-1703123456789',     // UUID
          'john.doe',                 // Username
          'John',                     // First name
          'Doe',                      // Last name
          'john.doe@gmail.com',       // Email
          '+1234567890',              // Phone
          '1990-01-01',               // DOB
          '123 Google St, Mountain View, CA', // Address
          'patient'                   // Role
        );

        this.currentUserSubject.next(backendUser);

        if (typeof window !== 'undefined') {
          localStorage.setItem('user', JSON.stringify(backendUser));
          localStorage.setItem('token', response.token);
        }
        console.log('-----------------------backendUser--------------------------')
        console.log(backendUser)
        this.router.navigate(['health-connect/dashboard']);
        return backendUser;
      }),
      catchError((error) => {
        console.error('🔐 Google login backend error:', error);
        // Fallback: create mock user data for Google login
        const backendUser = new UserModel(
          'google-' + Date.now(),
          user.email.split('@')[0],
          user.firstName,
          user.lastName,
          user.email,
          user.phone || '+1234567890',
          user.dob || '1990-01-01',
          user.address || '123 Google St, Mountain View, CA',
          'patient'
        );

        this.currentUserSubject.next(backendUser);
        if (typeof window !== 'undefined') {
          localStorage.setItem('user', JSON.stringify(backendUser));
        }
        console.log('-----------------------------------------------------------')
        console.log(backendUser)
        return [backendUser];
      })
    ).subscribe();
  }

  signup(password: string, userData: any): Observable<UserModel> {
    const signupData = {
      username: userData.username,
      firstName: userData.firstName,
      lastName: userData.lastName,
      email: userData.email,
      phone: userData.phone,
      dob: userData.dob,
      address: userData.address,
      userRole: userData.userRole,
      password: password
    };

    console.log('🔐 Attempting backend signup');
    return this.http.post<any>(`${this.baseUrl}/auth/signup`, signupData).pipe(
      map((response: any) => {
        if (!response || !response.user) {
          throw new Error('Invalid response from server');
        }

        const user = new UserModel(
          response.user.uuid,
          response.user.username,
          response.user.firstName,
          response.user.lastName,
          response.user.email,
          response.user.phone,
          response.user.dob,
          response.user.address,
          response.user.userRole?.toLowerCase() || response.user.userRole // Handle enum to string conversion
        );

        // Store user and token
        this.currentUserSubject.next(user);
        if (typeof window !== 'undefined') {
          localStorage.setItem('user', JSON.stringify(user));
          localStorage.setItem('token', response.token);
        }

        console.log('🔐 Backend signup successful, user:', user);
        return user;
      }),
      catchError((error) => {
        console.error('🔐 Backend signup failed:', error);

        // Provide specific error messages based on HTTP status
        let errorMessage = 'Signup failed. Please try again.';
        if (error.status === 400) {
          errorMessage = 'Invalid data provided. Please check your information.';
        } else if (error.status === 409) {
          errorMessage = 'User already exists with this email or username.';
        } else if (error.status === 0) {
          errorMessage = 'Cannot connect to server. Please check if the backend is running.';
        } else if (error.status >= 500) {
          errorMessage = 'Server error. Please try again later.';
        }

        throw new Error(errorMessage);
      })
    );
  }

  logout(): void {
    this.currentUserSubject.next(null);
    if (typeof window !== 'undefined') {
      localStorage.removeItem('user');
      localStorage.removeItem('token');
    }
    if (typeof google !== 'undefined' && google.accounts?.id) {
      google.accounts.id.disableAutoSelect();
    }
  }

  getCurrentUser(): UserModel | null {
    return this.currentUserSubject.value;
  }

  setCurrentUser(user: UserModel): void {
    this.currentUserSubject.next(user);
    if (typeof window !== 'undefined') {
      localStorage.setItem('user', JSON.stringify(user));
    }
  }

  isAuthenticated(): boolean {
    console.log('abc00000000000000000000000000')
    console.log(this.currentUserSubject.value)
    return this.currentUserSubject.value !== null;
  }
}
