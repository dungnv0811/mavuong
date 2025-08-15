import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { UserModel } from '../../shared/models/booking-docter-models';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  private mockUsers: { [key: string]: UserModel } = {
    // Doctor Users (based on mockDoctorProfiles)
    'doc-001': new UserModel('doc-001', 'elena.rodriguez', 'Elena', 'Rodriguez', 'elena.rodriguez@hospital.com', '(555) 001-0001', '1978-03-15', '123 Medical Plaza, Boston, MA 02115', 'doctor'),
    'doc-002': new UserModel('doc-002', 'michael.chen', 'Michael', 'Chen', 'michael.chen@hospital.com', '(555) 002-0002', '1982-07-22', '456 Stanford Ave, Palo Alto, CA 94301', 'doctor'),
    'doc-003': new UserModel('doc-003', 'sarah.kim', 'Sarah', 'Kim', 'sarah.kim@hospital.com', '(555) 003-0003', '1984-11-08', '789 UCLA Blvd, Los Angeles, CA 90024', 'doctor'),
    'doc-004': new UserModel('doc-004', 'david.green', 'David', 'Green', 'david.green@hospital.com', '(555) 004-0004', '1976-01-30', '321 Hopkins St, Baltimore, MD 21218', 'doctor'),
    'doc-005': new UserModel('doc-005', 'emily.white', 'Emily', 'White', 'emily.white@hospital.com', '(555) 005-0005', '1980-09-12', '654 Penn Ave, Philadelphia, PA 19104', 'doctor'),
    'doc-006': new UserModel('doc-006', 'james.brown', 'James', 'Brown', 'james.brown@hospital.com', '(555) 006-0006', '1974-05-18', '987 Mayo Clinic Dr, Rochester, MN 55905', 'doctor'),
    'doc-007': new UserModel('doc-007', 'olivia.perez', 'Olivia', 'Perez', 'olivia.perez@hospital.com', '(555) 007-0007', '1981-12-03', '147 Columbia St, New York, NY 10027', 'doctor'),
    'doc-008': new UserModel('doc-008', 'ben.carter', 'Ben', 'Carter', 'ben.carter@hospital.com', '(555) 008-0008', '1978-08-25', '258 Yale Ave, New Haven, CT 06511', 'doctor'),
    'doc-009': new UserModel('doc-009', 'sofia.garcia', 'Sofia', 'Garcia', 'sofia.garcia@hospital.com', '(555) 009-0009', '1983-04-14', '369 USC Way, Los Angeles, CA 90089', 'doctor'),
    
    // Patient Users
    'user-001': new UserModel('user-001', 'jane.doe', 'Jane', 'Doe', 'jane.doe@email.com', '(555) 123-4567', '1990-05-15', '123 Health Ave, Wellness City, CA 90210', 'patient'),
    'user-002': new UserModel('user-002', 'john.smith', 'John', 'Smith', 'john.smith@email.com', '(555) 234-5678', '1985-08-22', '456 Care Street, Medical Town, NY 10001', 'patient')
  };

  getUserById(userUuid: string): Observable<UserModel | null> {
    const user = this.mockUsers[userUuid] || null;
    return of(user).pipe(delay(500));
  }

  getAllUsers(): Observable<UserModel[]> {
    return of(Object.values(this.mockUsers)).pipe(delay(500));
  }

  getDoctorUsers(): Observable<UserModel[]> {
    const doctors = Object.values(this.mockUsers).filter(user => user.userRole === 'doctor');
    return of(doctors).pipe(delay(500));
  }

  getPatientUsers(): Observable<UserModel[]> {
    const patients = Object.values(this.mockUsers).filter(user => user.userRole === 'patient');
    return of(patients).pipe(delay(500));
  }

  createUser(user: UserModel): Observable<UserModel> {
    this.mockUsers[user.uuid] = user;
    return of(user).pipe(delay(1000));
  }

  updateUser(userUuid: string, user: UserModel): Observable<UserModel> {
    this.mockUsers[userUuid] = user;
    return of(user).pipe(delay(1000));
  }

  getUserByEmailOrName(emailOrName: string): Observable<UserModel | null> {
    const user = Object.values(this.mockUsers).find(u => 
      u.email.toLowerCase() === emailOrName.toLowerCase() ||
      u.username.toLowerCase() === emailOrName.toLowerCase() ||
      `${u.firstName} ${u.lastName}`.toLowerCase() === emailOrName.toLowerCase()
    );
    return of(user || null).pipe(delay(500));
  }
}