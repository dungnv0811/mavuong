export interface TimeSlot {
  time: string;
  isBooked: boolean;
  isDisabled: boolean;
}

export interface DoctorSchedule {
  doctorID: string;
  date: string;
  timeSlots: TimeSlot[];
}

export class DoctorScheduleModel implements DoctorSchedule {
  constructor(
    public doctorID: string,
    public date: string,
    public timeSlots: TimeSlot[]
  ) {}

  static createDefaultSchedule(doctorID: string, date: string): DoctorScheduleModel {
    const defaultTimes = [
      '09:00 AM', '09:30 AM', '10:00 AM', '10:30 AM', '11:00 AM', '11:30 AM',
      '01:00 PM', '01:30 PM', '02:00 PM', '02:30 PM', '03:00 PM', '03:30 PM', '04:00 PM'
    ];

    const currentDateTime = new Date();
    const selectedDate = new Date(date);
    const isToday = selectedDate.toDateString() === currentDateTime.toDateString();

    const timeSlots: TimeSlot[] = defaultTimes.map(time => {
      let isDisabled = false;
      
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
        isBooked: false,
        isDisabled
      };
    });

    return new DoctorScheduleModel(doctorID, date, timeSlots);
  }
}