# Health Check-up Appointment System - Demo Script

## Prerequisites
- Java 21 installed
- Maven installed
- Node.js and npm installed (for Angular frontend)

## Backend Setup & Run

1. **Navigate to project directory:**
   ```bash
   cd construction/appointment_booking
   ```

2. **Run Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Verify backend is running:**
   - Backend will start on http://localhost:8080
   - H2 Console available at http://localhost:8080/h2-console
   - API endpoints available at http://localhost:8080/api/*

## Frontend Setup & Run (Optional)

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start Angular development server:**
   ```bash
   npm start
   ```

4. **Access frontend:**
   - Frontend will be available at http://localhost:4200

## Demo Flow

### 1. Test Symptom Analysis API
```bash
curl -X POST http://localhost:8080/api/symptoms/analyze \
  -H "Content-Type: application/json" \
  -d '{"patientId": 1, "description": "I have chest pain and shortness of breath"}'
```

Expected response:
```json
{"suggestedSpecialties": ["cardiology"]}
```

### 2. Get All Doctors
```bash
curl http://localhost:8080/api/doctors
```

### 3. Get Doctors by Specialty
```bash
curl "http://localhost:8080/api/doctors?specialty=cardiology"
```

### 4. Book an Appointment
```bash
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "doctorId": 1,
    "appointmentDateTime": "2024-12-20T10:00:00",
    "notes": "Regular checkup"
  }'
```

### 5. Get Patient Appointments
```bash
curl "http://localhost:8080/api/appointments?patientId=1"
```

### 6. Confirm Appointment (Doctor action)
```bash
curl -X POST http://localhost:8080/api/appointments/1/confirm
```

## Frontend Demo (if running)

1. **Open browser:** http://localhost:4200

2. **Test Symptom Analysis:**
   - Enter symptoms like "chest pain and palpitations"
   - Click "Analyze Symptoms"
   - See suggested specialties

3. **Browse Doctors:**
   - Filter by specialty
   - View doctor profiles and ratings

4. **Book Appointment:**
   - Select a doctor
   - Choose date/time
   - Add notes
   - Confirm booking

5. **View Appointments:**
   - Enter Patient ID (use 1)
   - Load appointments
   - See booking status

## Database Access

- **H2 Console:** http://localhost:8080/h2-console
- **JDBC URL:** jdbc:h2:mem:healthapp
- **Username:** sa
- **Password:** (empty)

## Sample Data

The system comes pre-loaded with 5 doctors:
- Dr. John Smith (Cardiology)
- Dr. Sarah Johnson (Dermatology)
- Dr. Mike Wilson (Orthopedics)
- Dr. Lisa Brown (Neurology)
- Dr. David Lee (General Practice)

## Troubleshooting

1. **Port conflicts:** Change server.port in application.yml
2. **CORS issues:** Frontend configured for localhost:4200
3. **Database issues:** H2 is in-memory, data resets on restart