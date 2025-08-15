import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { DEFAULT_LOGO } from '../../features/constants/common.constants';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './shell.component.html',
  styleUrls: ['./shell.component.scss']
})
export class ShellComponent {
  protected readonly DEFAULT_LOGO = DEFAULT_LOGO;

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['']);
  }

  navigateToProfile(): void {
    const user = this.authService.getCurrentUser();
    if (user?.userRole === 'doctor') {
      this.router.navigate(['/health-connect/doctor/profile']);
    } else {
      this.router.navigate(['/health-connect/profile']);
    }
  }

  isDoctorUser(): boolean {
    const user = this.authService.getCurrentUser();
    return user?.userRole === 'doctor';
  }
}
