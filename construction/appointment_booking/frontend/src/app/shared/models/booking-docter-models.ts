import {DEFAULT_AVATAR_FEMALE, DEFAULT_AVATAR_MALE} from '../../features/constants/common.constants';

export enum AppointmentStatus {
  UPCOMING = 'Upcoming',
  COMPLETED = 'Completed',
  CANCELLED = 'Cancelled',
  BOOKED = 'Booked'
}

export interface Appointment {
  uuid: string;
  doctorID: string;
  userID: string;
  doctor: string; specialty: string; dateTime: string; place: string; status: AppointmentStatus; avatarLink: string;
}

export class AppointmentModel implements Appointment {
  constructor(
    public uuid: string,
    public doctorID: string,
    public userID: string,
    public doctor: string,
    public specialty: string,
    public dateTime: string,
    public place: string,
    public status: AppointmentStatus = AppointmentStatus.BOOKED,
    public avatarLink: string = DEFAULT_AVATAR_FEMALE,
  ) {}
}

export interface DoctorBookingInfo {
  doctorID: string;
  name: string;
  specialty: string;
  rating: number;
  reviews: number;
  nextAvailable: string;
  avatar: string;
}

export class DoctorBookingInfoModels implements DoctorBookingInfo {
  constructor(
    public doctorID: string,
    public name: string,
    public specialty: string,
    public rating: number,
    public reviews: number,
    public nextAvailable: string,
    public avatar: string,
  ) {

  }
}

export interface User {
  uuid: string;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  dob: string;
  address: string;
  userRole: string;
}

export class UserModel implements User {
  constructor(
    public uuid: string,
    public username: string,
    public firstName: string,
    public lastName: string,
    public email: string,
    public phone: string,
    public dob: string,
    public address: string,
    public userRole: string = 'patient',
  ) {}
}


