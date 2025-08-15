# 🔐 Swagger UI Authentication Demo

## 🚀 How to Use JWT Authentication in Swagger UI

### Step 1: Start Your Backend
```bash
cd D:\techtalk\mavuong\construction\appointment_booking
mvn spring-boot:run
```

### Step 2: Open Swagger UI
Visit: **http://localhost:8080/swagger-ui.html**

You should now see:
- ✅ **"Authorize" button** at the top right
- ✅ **Lock icons** 🔒 on protected endpoints
- ✅ Organized API sections with descriptions

### Step 3: Login to Get JWT Token

#### Option A: Use Swagger UI Login Endpoint
1. Expand **"Authentication"** section
2. Click on **POST /api/auth/login**
3. Click **"Try it out"**
4. Enter demo credentials:
```json
{
  "username": "patient1",
  "password": "password"
}
```
5. Click **"Execute"**
6. **Copy the token** from the response

#### Option B: Use cURL (Alternative)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "patient1", "password": "password"}'
```

### Step 4: Authorize in Swagger UI
1. Click the **🔓 "Authorize"** button at the top
2. In the **bearer-key** field, enter:
   ```
   Bearer YOUR_JWT_TOKEN_HERE
   ```
   *(Make sure to include "Bearer " before your token)*
3. Click **"Authorize"**
4. Click **"Close"**

You should now see **🔒 "Authorized"** at the top!

### Step 5: Test Protected Endpoints

#### Test Patient Endpoints (with patient1 token):
- ✅ **GET /api/appointments** - Get appointments
- ✅ **POST /api/appointments** - Book appointment
- ✅ **POST /api/symptoms/analyze** - Analyze symptoms

#### Test Doctor Role (login as doctor1):
1. **Logout**: Click "Authorize" → "Logout"
2. **Login as doctor**: Use `doctor1/password`
3. **Test access**:
   - ✅ **GET /api/appointments** - Should work
   - ❌ **POST /api/symptoms/analyze** - Should fail (403 Forbidden)

## 📋 Demo User Credentials

| Username | Password | Role | Access |
|----------|----------|------|--------|
| `patient1` | `password` | PATIENT | Symptoms + Appointments |
| `doctor1` | `password` | DOCTOR | Appointments only |
| `admin` | `password` | ADMIN | Everything |

## 🎯 What You'll See in Swagger UI

### 🔓 **Public Endpoints** (No lock icon)
- **POST /api/auth/login** - Login
- **POST /api/auth/register** - Register
- **GET /api/doctors** - List doctors

### 🔒 **Protected Endpoints** (Lock icon)
- **GET /api/auth/user** - Current user info
- **All appointment endpoints**
- **Symptom analysis** (patients only)
- **Doctor schedule updates**

### 🏷️ **Organized Sections**
- **Authentication** - Login/register operations
- **Appointments** - Appointment management
- **Doctors** - Doctor information
- **Symptoms** - Symptom analysis

## 🧪 Testing Scenarios

### Scenario 1: Patient Workflow
1. Login as **patient1**
2. Authorize with JWT token
3. Test symptom analysis:
```json
{
  "patientId": 1,
  "description": "I have a headache and feeling tired"
}
```
4. Test appointment booking:
```json
{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDateTime": "2024-12-25T10:00:00",
  "notes": "Regular checkup"
}
```

### Scenario 2: Doctor Workflow
1. Login as **doctor1**
2. Authorize with JWT token
3. Get doctor appointments:
   - Use **GET /api/appointments?doctorId=1**
4. Try symptom analysis (should fail):
   - Should get **403 Forbidden** error

### Scenario 3: Security Testing
1. Try protected endpoints **without authorization**
   - Should get **401 Unauthorized**
2. Try with **invalid token**
   - Should get **401 Unauthorized**
3. Try **role-based restrictions**
   - Doctor trying symptom analysis = **403 Forbidden**

## 🔧 Troubleshooting

### Common Issues:

1. **"401 Unauthorized" errors**
   - Make sure you clicked "Authorize" and entered valid token
   - Check token format: `Bearer eyJhbGci...`
   - Token expires after 24 hours

2. **"403 Forbidden" errors**
   - User doesn't have required role
   - Expected behavior for role restrictions

3. **No "Authorize" button**
   - Restart backend after configuration changes
   - Clear browser cache

4. **Token not working**
   - Ensure token includes "Bearer " prefix
   - Copy complete token without line breaks

## 🎉 Success Indicators

You'll know it's working when:
- ✅ **Authorize button** appears in Swagger UI
- ✅ **Lock icons** show on protected endpoints
- ✅ **Protected endpoints work** after authorization
- ✅ **Role-based access** is enforced correctly
- ✅ **Public endpoints work** without authorization

Your Swagger UI now has full JWT authentication support! 🚀

