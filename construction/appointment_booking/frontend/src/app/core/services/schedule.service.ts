import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { delay, map, catchError } from 'rxjs/operators';
import { DoctorScheduleModel, TimeSlot } from '../../shared/models/schedule.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {
  private http = inject(HttpClient);
  private baseUrl = environment.apiUrl;
  
  private mockBookedSlots: { [key: string]: string[] } = {
    // Format: 'doctorID-date': ['time1', 'time2']
    'doc-001-2024-12-20': ['09:00 AM', '02:00 PM'],
    'doc-001-2024-12-21': ['10:30 AM', '03:00 PM'],
    'doc-002-2024-12-20': ['11:00 AM', '01:30 PM'],
    'doc-003-2024-12-20': ['09:30 AM', '02:30 PM'],
  };

  private getAvailableTimesForDate(date: string): string[] {
    const dayOfWeek = new Date(date).getDay(); // 0=Sunday, 1=Monday, etc.
    
    switch(dayOfWeek) {
      case 0: // Sunday
        return ['10:00 AM', '11:00 AM', '02:00 PM', '03:00 PM'];
      case 1: // Monday
        return ['09:00 AM', '09:30 AM', '10:00 AM', '10:30 AM', '11:00 AM', '01:00 PM', '01:30 PM', '02:00 PM', '02:30 PM', '03:00 PM'];
      case 2: // Tuesday
        return ['08:30 AM', '09:00 AM', '10:00 AM', '11:00 AM', '01:00 PM', '02:00 PM', '03:00 PM', '04:00 PM'];
      case 3: // Wednesday
        return ['09:00 AM', '10:00 AM', '11:00 AM', '11:30 AM', '01:30 PM', '02:30 PM', '03:30 PM'];
      case 4: // Thursday
        return ['08:00 AM', '09:00 AM', '09:30 AM', '10:30 AM', '11:30 AM', '01:00 PM', '02:00 PM', '03:00 PM', '04:00 PM', '04:30 PM'];
      case 5: // Friday
        return ['09:00 AM', '10:00 AM', '11:00 AM', '01:00 PM', '02:00 PM'];
      case 6: // Saturday
        return ['09:00 AM', '10:00 AM', '11:00 AM', '12:00 PM'];
      default:
        return ['09:00 AM', '10:00 AM', '11:00 AM', '01:00 PM', '02:00 PM', '03:00 PM'];
    }
  }

  getDoctorSchedule(doctorID: string, date: string): Observable<DoctorScheduleModel> {
    console.log('📅 Getting doctor schedule for:', doctorID, 'on', date);
    
    return this.http.get<any>(`${this.baseUrl}/schedules/${doctorID}?date=${date}`).pipe(
      map((response: any) => {
        console.log('📅 Backend schedule response:', response);
        
        const schedule = response.schedule || response;
        const timeSlots: TimeSlot[] = schedule.timeSlots?.map((slot: any) => ({
          time: slot.time,
          isBooked: slot.isBooked || false,
          isDisabled: slot.isDisabled || false
        })) || [];
        
        return new DoctorScheduleModel(doctorID, date, timeSlots);
      }),
      catchError((error) => {
        console.warn('📅 Backend call failed for schedule, using mock data:', error);
        
        // Fallback to mock schedule logic
        const availableTimes = this.getAvailableTimesForDate(date);
        const key = `${doctorID}-${date}`;
        const bookedTimes = this.mockBookedSlots[key] || [];
        
        const currentDateTime = new Date();
        const selectedDate = new Date(date);
        const isToday = selectedDate.toDateString() === currentDateTime.toDateString();

        const timeSlots: TimeSlot[] = availableTimes.map(time => {
          let isDisabled = false;
          let isBooked = bookedTimes.includes(time);
          
          if (isToday) {
            const [timeStr, period] = time.split(' ');
            const [hours, minutes] = timeStr.split(':').map(Number);
            let hour24 = hours;
            
            if (period === 'PM' && hours !== 12) hour24 += 12;
            if (period === 'AM' && hours === 12) hour24 = 0;
            
            const slotDateTime = new Date(selectedDate);
            slotDateTime.setHours(hour24, minutes, 0, 0);
            
            isDisabled = slotDateTime <= currentDateTime;
          }

          return {
            time,
            isBooked,
            isDisabled
          };
        });

        const schedule = new DoctorScheduleModel(doctorID, date, timeSlots);
        return of(schedule).pipe(delay(500));
      })
    );
  }

  bookTimeSlot(doctorID: string, date: string, time: string): Observable<boolean> {
    const key = `${doctorID}-${date}`;
    if (!this.mockBookedSlots[key]) {
      this.mockBookedSlots[key] = [];
    }
    
    if (!this.mockBookedSlots[key].includes(time)) {
      this.mockBookedSlots[key].push(time);
      return of(true).pipe(delay(1000));
    }
    
    return of(false).pipe(delay(1000));
  }

  releaseTimeSlot(doctorID: string, date: string, time: string): Observable<boolean> {
    const key = `${doctorID}-${date}`;
    if (this.mockBookedSlots[key]) {
      const index = this.mockBookedSlots[key].indexOf(time);
      if (index > -1) {
        this.mockBookedSlots[key].splice(index, 1);
        return of(true).pipe(delay(500));
      }
    }
    return of(false).pipe(delay(500));
  }

  getAvailableSlots(doctorID: string, date: string): Observable<TimeSlot[]> {
    return this.getDoctorSchedule(doctorID, date).pipe(
      map((schedule: DoctorScheduleModel) => 
        schedule.timeSlots.filter(slot => !slot.isBooked && !slot.isDisabled)
      ),
      delay(500)
    );
  }
}