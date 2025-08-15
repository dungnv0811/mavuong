// src/app/features/home/home.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  faqs = [
    { q: 'How do I book an appointment?', a: 'You can book an appointment via our online booking system or mobile app.' },
    { q: 'Is my personal health information secure?', a: 'Yes, all your information is encrypted and stored securely.' },
    { q: 'Can I reschedule or cancel appointments?', a: 'Yes, simply log in and manage your appointments from your profile.' },
    { q: 'How do I find a specialist doctor?', a: 'Search our doctor listing and filter by specialty.' },
  ];
  openIndex: number | null = null;

  toggleFaq(i: number) {
    this.openIndex = this.openIndex === i ? null : i;
  }
}
