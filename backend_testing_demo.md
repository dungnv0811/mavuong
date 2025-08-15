# Backend Authentication Testing Demo

## 🚀 Starting the Backend

First, start your Spring Boot application:

```bash
cd D:\techtalk\mavuong\construction\appointment_booking
mvn spring-boot:run
```

The backend will be available at: `http://localhost:8080`

## 📋 Available Endpoints

### Authentication Endpoints (Public)
- `POST /api/auth/login` - Login with username/password
- `POST /api/auth/register` - Register new user
- `GET /api/auth/user` - Get current user info (requires token)

### Protected Endpoints
- `GET /api/doctors` - List doctors (PUBLIC)
- `POST /api/appointments` - Book appointment (PATIENT/DOCTOR only)
- `GET /api/appointments` - Get appointments (PATIENT/DOCTOR only)
- `POST /api/symptoms/analyze` - Analyze symptoms (PATIENT only)

### Admin/Debug Endpoints
- `GET /h2-console` - Database console
- `GET /swagger-ui.html` - API documentation

## 🔐 Demo Users

Your backend has these pre-configured users:

| Username | Password | Role |
|----------|----------|------|
| patient1 | password | PATIENT |
| doctor1  | password | DOCTOR |
| admin    | password | ADMIN |

---

## 🧪 Testing Methods

### Method 1: Using cURL Commands

#### 1. Test Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "patient1", "password": "password"}'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "message": "Login successful",
  "username": "patient1"
}
```

#### 2. Test Protected Endpoint (Without Token)
```bash
curl -X GET http://localhost:8080/api/appointments?patientId=1
```

**Expected Response:** `401 Unauthorized`

#### 3. Test Protected Endpoint (With Token)
```bash
# Replace YOUR_JWT_TOKEN with the token from login response
curl -X GET http://localhost:8080/api/appointments?patientId=1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 4. Test Role-Based Access
```bash
# This should work for PATIENT role
curl -X POST http://localhost:8080/api/symptoms/analyze \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_PATIENT_TOKEN" \
  -d '{"patientId": 1, "description": "I have a headache"}'

# This should fail for DOCTOR role (403 Forbidden)
curl -X POST http://localhost:8080/api/symptoms/analyze \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_DOCTOR_TOKEN" \
  -d '{"patientId": 1, "description": "I have a headache"}'
```

---

### Method 2: Using Postman

#### Step 1: Create a New Collection
1. Open Postman
2. Create new collection: "Health App API"

#### Step 2: Add Authentication Request
- **Method:** POST
- **URL:** `http://localhost:8080/api/auth/login`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "username": "patient1",
  "password": "password"
}
```

#### Step 3: Save Token as Variable
1. In the login request, go to "Tests" tab
2. Add this script to auto-save the token:
```javascript
if (pm.response.json().token) {
    pm.globals.set("jwt_token", pm.response.json().token);
}
```

#### Step 4: Test Protected Endpoints
- **Method:** GET
- **URL:** `http://localhost:8080/api/appointments?patientId=1`
- **Headers:** `Authorization: Bearer {{jwt_token}}`

---

### Method 3: Using Swagger UI

1. Visit: `http://localhost:8080/swagger-ui.html`
2. Find the `/api/auth/login` endpoint
3. Click "Try it out"
4. Enter credentials:
   ```json
   {
     "username": "patient1",
     "password": "password"
   }
   ```
5. Copy the token from response
6. Click "Authorize" button at top
7. Enter: `Bearer YOUR_JWT_TOKEN`
8. Now test other endpoints!

---

## 🔍 Step-by-Step Testing Scenarios

### Scenario 1: Patient Login and Book Appointment

1. **Login as Patient:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "patient1", "password": "password"}'
```

2. **Get Current User Info:**
```bash
curl -X GET http://localhost:8080/api/auth/user \
  -H "Authorization: Bearer YOUR_TOKEN"
```

3. **List Available Doctors:**
```bash
curl -X GET http://localhost:8080/api/doctors
```

4. **Analyze Symptoms:**
```bash
curl -X POST http://localhost:8080/api/symptoms/analyze \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "patientId": 1,
    "description": "I have been experiencing headaches and fatigue for the past week"
  }'
```

5. **Book an Appointment:**
```bash
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "patientId": 1,
    "doctorId": 1,
    "appointmentDateTime": "2024-12-20T10:00:00",
    "notes": "Regular checkup"
  }'
```

6. **Get Patient Appointments:**
```bash
curl -X GET "http://localhost:8080/api/appointments?patientId=1" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Scenario 2: Doctor Login and View Appointments

1. **Login as Doctor:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "doctor1", "password": "password"}'
```

2. **View Doctor's Appointments:**
```bash
curl -X GET "http://localhost:8080/api/appointments?doctorId=1" \
  -H "Authorization: Bearer YOUR_DOCTOR_TOKEN"
```

3. **Try to Access Patient-Only Feature (Should Fail):**
```bash
curl -X POST http://localhost:8080/api/symptoms/analyze \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_DOCTOR_TOKEN" \
  -d '{"patientId": 1, "description": "test"}'
```

### Scenario 3: Testing Security Violations

1. **Invalid Credentials:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "patient1", "password": "wrongpassword"}'
```

2. **Expired/Invalid Token:**
```bash
curl -X GET http://localhost:8080/api/appointments?patientId=1 \
  -H "Authorization: Bearer invalid_token"
```

3. **Missing Authorization Header:**
```bash
curl -X GET http://localhost:8080/api/appointments?patientId=1
```

---

## 🗄️ Database Console Testing

1. Visit: `http://localhost:8080/h2-console`
2. Use these settings:
   - **JDBC URL:** `jdbc:h2:mem:healthapp`
   - **Username:** `sa`
   - **Password:** (leave empty)

3. Query users and data:
```sql
-- See all tables
SHOW TABLES;

-- Check appointments
SELECT * FROM appointments;

-- Check doctors
SELECT * FROM doctors;
```

---

## 🎯 Expected Test Results

### ✅ Successful Cases
- Login returns JWT token
- Protected endpoints work with valid token
- Role-based access works correctly
- Public endpoints (doctors list) work without token

### ❌ Error Cases
- 401 Unauthorized for missing/invalid tokens
- 403 Forbidden for insufficient permissions
- 400 Bad Request for invalid login credentials

---

## 🐛 Troubleshooting Tips

### Common Issues:

1. **"401 Unauthorized"**
   - Check if token is included in Authorization header
   - Verify token format: `Bearer YOUR_TOKEN`
   - Token might be expired (24 hours)

2. **"403 Forbidden"**
   - User doesn't have required role
   - Check role mapping in JWT token

3. **"404 Not Found"**
   - Verify endpoint URLs
   - Check if Spring Boot is running on port 8080

4. **CORS Issues**
   - Frontend should be on `http://localhost:4200`
   - Backend configured for this origin

### Debug Commands:
```bash
# Check if backend is running
curl http://localhost:8080/api/doctors

# Check Spring Boot logs
tail -f logs/spring.log

# Verify JWT token content (decode at jwt.io)
echo "YOUR_TOKEN" | base64 -d
```

