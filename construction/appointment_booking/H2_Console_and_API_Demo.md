# H2 Console and API Demo

This guide shows how to open the H2 console and exercise the REST API with curl.

## Start the app

```bash
# From construction/appointment_booking
mvn spring-boot:run
```

- Base URL: `http://localhost:8080`
- Dev security is open (`security.cognito.enabled=false`), so no JWT is required.

## H2 Console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:healthapp`
- User: `sa`
- Password: (leave empty)

Notes
- Schema and sample data are loaded automatically from `src/main/resources/schema.sql` and `src/main/resources/data.sql`.
- Example queries:
  ```sql
  SELECT * FROM doctors;
  SELECT * FROM doctor_schedules;
  SELECT * FROM appointments;
  SELECT * FROM symptom_analyses;
  SELECT * FROM recommendations;
  ```

## API calls with curl

### Doctors

- List doctors
```bash
curl -s http://localhost:8080/api/doctors | jq
```

- Get doctor by id
```bash
curl -s http://localhost:8080/api/doctors/1 | jq
```

- Doctor availability
```bash
curl -s http://localhost:8080/api/doctors/1/availability | jq
```

- Add/Update a doctor schedule
```bash
curl -s -X POST http://localhost:8080/api/doctors/1/schedule \
  -H "Content-Type: application/json" \
  -d '{"dayOfWeek":"FRIDAY","startTime":"09:00:00","endTime":"11:00:00"}' | jq
```

### Symptoms

- Analyze symptoms
```bash
curl -s -X POST http://localhost:8080/api/symptoms/analyze \
  -H "Content-Type: application/json" \
  -d '{"patientId":1003,"description":"I feel chest tightness when running"}' | jq
```

### Appointments

- Book appointment
```bash
curl -s -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{"patientId":1001,"doctorId":1,"appointmentDateTime":"2025-08-15T10:30:00","notes":"Follow up"}' | jq
```

- List appointments (patient view)
```bash
curl -s "http://localhost:8080/api/appointments?patientId=1001" | jq
```

- List appointments (doctor view)
```bash
curl -s "http://localhost:8080/api/appointments?doctorId=1" | jq
```

- Get appointment by id (seeded IDs start at 1)
```bash
curl -s http://localhost:8080/api/appointments/1 | jq
```

- Confirm appointment
```bash
curl -s -X POST http://localhost:8080/api/appointments/1/confirm | jq
```

- Reschedule appointment
```bash
curl -s -X PUT http://localhost:8080/api/appointments/1 \
  -H "Content-Type: application/json" \
  -d '{"newDateTime":"2025-08-16T11:00:00"}' | jq
```

- Cancel appointment
```bash
curl -s -X DELETE http://localhost:8080/api/appointments/1 | jq
```

## Troubleshooting
- If the app fails to build or run with Maven, make sure you are using Java 17+ (recommended 21).
- If H2 console doesn’t open, confirm `spring.h2.console.enabled=true` and the path `/h2-console` in `application.yml`.
