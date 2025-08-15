import { Injectable, inject } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { switchMap, map } from 'rxjs/operators';
import { UserModel } from '../../shared/models/booking-docter-models';
import {UserService} from "../services/user.service";

declare const google: any;

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userService = inject(UserService);
  private currentUserSubject = new BehaviorSubject<UserModel | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    if (typeof window !== 'undefined') {
      const savedUser = localStorage.getItem('user');
      if (savedUser) {
        this.currentUserSubject.next(JSON.parse(savedUser));
      }
    }
  }

  login(email: string, password: string): Observable<UserModel> {
    return this.userService.getUserByEmailOrName(email).pipe(
      switchMap(user => {
        return new Observable<UserModel>(observer => {
          setTimeout(() => {
            if (user) {
              // Login success - user found
              this.currentUserSubject.next(user);
              if (typeof window !== 'undefined') {
                localStorage.setItem('user', JSON.stringify(user));
              }
              observer.next(user);
              observer.complete();
            } else {
              // Login failed - user not found
              observer.error(new Error('Invalid email or user not found'));
            }
          }, 1000);
        });
      })
    );
  }

  loginWithGoogle(user: UserModel): void {
    // Check if user exists by email, if not create new one
    this.userService.getUserByEmailOrName(user.email).subscribe({
      next: (existingUser) => {
        const userToLogin = existingUser || user;
        
        // If user doesn't exist, create new one
        if (!existingUser) {
          this.userService.createUser(user).subscribe();
        }
        
        this.currentUserSubject.next(userToLogin);
        if (typeof window !== 'undefined') {
          localStorage.setItem('user', JSON.stringify(userToLogin));
        }
      },
      error: () => {
        // Fallback: use provided user
        this.currentUserSubject.next(user);
        if (typeof window !== 'undefined') {
          localStorage.setItem('user', JSON.stringify(user));
        }
      }
    });
  }

  signup(password: string, userData: any): Observable<UserModel> {
    // Simulate backend API call
    return new Observable(observer => {
      setTimeout(() => {
        // Mock user creation corresponding to UserModel
        const newUser = new UserModel(
          'user-' + Date.now(),
          userData.username,
          userData.firstName,
          userData.lastName,
          userData.email,
          userData.phone,
          userData.dob,
          userData.address,
          userData.userRole
        );

        this.currentUserSubject.next(newUser);
        if (typeof window !== 'undefined') {
          localStorage.setItem('user', JSON.stringify(newUser));
        }
        observer.next(newUser);
        observer.complete();
      }, 1500);
    });
  }

  logout(): void {
    this.currentUserSubject.next(null);
    if (typeof window !== 'undefined') {
      localStorage.removeItem('user');
    }
    if (typeof google !== 'undefined' && google.accounts?.id) {
      google.accounts.id.disableAutoSelect();
    }
  }

  getCurrentUser(): UserModel | null {
    return this.currentUserSubject.value;
  }

  isAuthenticated(): boolean {
    return this.currentUserSubject.value !== null;
  }
}
