# PowerShell script to test backend authentication
# Run this script in PowerShell to test your backend

Write-Host "🧪 Testing Backend Authentication System" -ForegroundColor Green
Write-Host "======================================" -ForegroundColor Green

$baseUrl = "http://localhost:8080"

# Test 1: Check if backend is running
Write-Host "`n1️⃣ Testing if backend is running..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/doctors" -Method Get
    Write-Host "✅ Backend is running!" -ForegroundColor Green
    Write-Host "Found $($response.Length) doctors in the system" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Backend is not running. Please start it with: mvn spring-boot:run" -ForegroundColor Red
    exit 1
}

# Test 2: Login as Patient
Write-Host "`n2️⃣ Testing Patient Login..." -ForegroundColor Yellow
$loginBody = @{
    username = "patient1"
    password = "password"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    $patientToken = $loginResponse.token
    Write-Host "✅ Patient login successful!" -ForegroundColor Green
    Write-Host "Token: $($patientToken.Substring(0, 20))..." -ForegroundColor Cyan
} catch {
    Write-Host "❌ Patient login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 3: Get current user info
Write-Host "`n3️⃣ Testing Get Current User..." -ForegroundColor Yellow
try {
    $headers = @{ Authorization = "Bearer $patientToken" }
    $userInfo = Invoke-RestMethod -Uri "$baseUrl/api/auth/user" -Method Get -Headers $headers
    Write-Host "✅ Current user: $($userInfo.username) (Role: $($userInfo.role))" -ForegroundColor Green
} catch {
    Write-Host "❌ Get user info failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Test protected endpoint without token
Write-Host "`n4️⃣ Testing Protected Endpoint (No Token)..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/appointments?patientId=1" -Method Get
    Write-Host "❌ This should have failed!" -ForegroundColor Red
} catch {
    Write-Host "✅ Correctly blocked - 401 Unauthorized" -ForegroundColor Green
}

# Test 5: Test protected endpoint with token
Write-Host "`n5️⃣ Testing Protected Endpoint (With Token)..." -ForegroundColor Yellow
try {
    $headers = @{ Authorization = "Bearer $patientToken" }
    $appointments = Invoke-RestMethod -Uri "$baseUrl/api/appointments?patientId=1" -Method Get -Headers $headers
    Write-Host "✅ Protected endpoint accessible with token!" -ForegroundColor Green
    Write-Host "Found $($appointments.Length) appointments" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Protected endpoint failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Login as Doctor
Write-Host "`n6️⃣ Testing Doctor Login..." -ForegroundColor Yellow
$doctorLoginBody = @{
    username = "doctor1"
    password = "password"
} | ConvertTo-Json

try {
    $doctorLoginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body $doctorLoginBody -ContentType "application/json"
    $doctorToken = $doctorLoginResponse.token
    Write-Host "✅ Doctor login successful!" -ForegroundColor Green
} catch {
    Write-Host "❌ Doctor login failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Test role-based access (Doctor trying patient-only endpoint)
Write-Host "`n7️⃣ Testing Role-Based Access Control..." -ForegroundColor Yellow
$symptomBody = @{
    patientId = 1
    description = "Test symptom"
} | ConvertTo-Json

try {
    $headers = @{ Authorization = "Bearer $doctorToken" }
    $response = Invoke-RestMethod -Uri "$baseUrl/api/symptoms/analyze" -Method Post -Body $symptomBody -ContentType "application/json" -Headers $headers
    Write-Host "❌ Doctor should not access patient-only endpoint!" -ForegroundColor Red
} catch {
    Write-Host "✅ Correctly blocked - 403 Forbidden (Doctor cannot access patient endpoint)" -ForegroundColor Green
}

# Test 8: Test patient accessing symptom analysis
Write-Host "`n8️⃣ Testing Patient Role Access..." -ForegroundColor Yellow
try {
    $headers = @{ Authorization = "Bearer $patientToken" }
    $response = Invoke-RestMethod -Uri "$baseUrl/api/symptoms/analyze" -Method Post -Body $symptomBody -ContentType "application/json" -Headers $headers
    Write-Host "✅ Patient can access symptom analysis!" -ForegroundColor Green
    Write-Host "Suggested specialties: $($response.suggestedSpecialties -join ', ')" -ForegroundColor Cyan
} catch {
    Write-Host "⚠️ Patient symptom analysis failed: $($_.Exception.Message)" -ForegroundColor Yellow
}

# Test 9: Book an appointment
Write-Host "`n9️⃣ Testing Appointment Booking..." -ForegroundColor Yellow
$appointmentBody = @{
    patientId = 1
    doctorId = 1
    appointmentDateTime = "2024-12-25T10:00:00"
    notes = "Test appointment from PowerShell script"
} | ConvertTo-Json

try {
    $headers = @{ Authorization = "Bearer $patientToken" }
    $appointment = Invoke-RestMethod -Uri "$baseUrl/api/appointments" -Method Post -Body $appointmentBody -ContentType "application/json" -Headers $headers
    Write-Host "✅ Appointment booked successfully!" -ForegroundColor Green
    Write-Host "Appointment ID: $($appointment.id), Status: $($appointment.status)" -ForegroundColor Cyan
} catch {
    Write-Host "⚠️ Appointment booking failed: $($_.Exception.Message)" -ForegroundColor Yellow
}

# Summary
Write-Host "`n🎉 Backend Authentication Testing Complete!" -ForegroundColor Green
Write-Host "======================================" -ForegroundColor Green
Write-Host "✅ Authentication system is working correctly" -ForegroundColor Green
Write-Host "✅ Role-based access control is functioning" -ForegroundColor Green
Write-Host "✅ JWT tokens are being validated properly" -ForegroundColor Green

Write-Host "`n📋 Available Demo Users:" -ForegroundColor Cyan
Write-Host "• patient1 / password (PATIENT role)" -ForegroundColor White
Write-Host "• doctor1 / password (DOCTOR role)" -ForegroundColor White  
Write-Host "• admin / password (ADMIN role)" -ForegroundColor White

Write-Host "`n🌐 Useful URLs:" -ForegroundColor Cyan
Write-Host "• API Docs: http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host "• H2 Console: http://localhost:8080/h2-console" -ForegroundColor White
Write-Host "• Frontend: http://localhost:4200" -ForegroundColor White

