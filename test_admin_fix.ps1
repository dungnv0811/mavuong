# PowerShell script to test admin access fix

Write-Host "🔧 Testing Admin Access Fix" -ForegroundColor Green
Write-Host "===========================" -ForegroundColor Green

$baseUrl = "http://localhost:8080"

# Test 1: Login as admin
Write-Host "`n1️⃣ Testing Admin Login..." -ForegroundColor Yellow
$adminLoginBody = @{
    username = "admin"
    password = "password"
} | ConvertTo-Json

try {
    $adminLoginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body $adminLoginBody -ContentType "application/json"
    $adminToken = $adminLoginResponse.token
    Write-Host "✅ Admin login successful!" -ForegroundColor Green
    Write-Host "Token: $($adminToken.Substring(0, 20))..." -ForegroundColor Cyan
} catch {
    Write-Host "❌ Admin login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 2: Debug token information
Write-Host "`n2️⃣ Debugging Admin Token..." -ForegroundColor Yellow
try {
    $headers = @{ Authorization = "Bearer $adminToken" }
    $debugInfo = Invoke-RestMethod -Uri "$baseUrl/api/auth/debug" -Method Get -Headers $headers
    Write-Host "✅ Debug info retrieved!" -ForegroundColor Green
    Write-Host "Username: $($debugInfo.username)" -ForegroundColor Cyan
    Write-Host "Role from token: $($debugInfo.roleFromToken)" -ForegroundColor Cyan
    Write-Host "Role from user: $($debugInfo.roleFromUser)" -ForegroundColor Cyan
    Write-Host "Token valid: $($debugInfo.tokenValid)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Debug info failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Test admin accessing appointments
Write-Host "`n3️⃣ Testing Admin Appointment Access..." -ForegroundColor Yellow
try {
    $headers = @{ Authorization = "Bearer $adminToken" }
    $appointments = Invoke-RestMethod -Uri "$baseUrl/api/appointments?patientId=1" -Method Get -Headers $headers
    Write-Host "✅ Admin can access appointments!" -ForegroundColor Green
    Write-Host "Found $($appointments.Length) appointments" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Admin appointment access failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

# Test 4: Test admin accessing symptom analysis
Write-Host "`n4️⃣ Testing Admin Symptom Analysis Access..." -ForegroundColor Yellow
$symptomBody = @{
    patientId = 1
    description = "Admin test symptom"
} | ConvertTo-Json

try {
    $headers = @{ Authorization = "Bearer $adminToken" }
    $symptomResponse = Invoke-RestMethod -Uri "$baseUrl/api/symptoms/analyze" -Method Post -Body $symptomBody -ContentType "application/json" -Headers $headers
    Write-Host "✅ Admin can access symptom analysis!" -ForegroundColor Green
    Write-Host "Suggested specialties: $($symptomResponse.suggestedSpecialties -join ', ')" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Admin symptom analysis failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

# Test 5: Test admin booking appointment
Write-Host "`n5️⃣ Testing Admin Appointment Booking..." -ForegroundColor Yellow
$appointmentBody = @{
    patientId = 1
    doctorId = 1
    appointmentDateTime = "2024-12-30T14:00:00"
    notes = "Admin test appointment"
} | ConvertTo-Json

try {
    $headers = @{ Authorization = "Bearer $adminToken" }
    $appointment = Invoke-RestMethod -Uri "$baseUrl/api/appointments" -Method Post -Body $appointmentBody -ContentType "application/json" -Headers $headers
    Write-Host "✅ Admin can book appointments!" -ForegroundColor Green
    Write-Host "Appointment ID: $($appointment.id), Status: $($appointment.status)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Admin appointment booking failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

Write-Host "`n🎉 Admin Access Testing Complete!" -ForegroundColor Green
Write-Host "=================================" -ForegroundColor Green

Write-Host "`n📋 Next Steps:" -ForegroundColor Cyan
Write-Host "1. Restart your Spring Boot application" -ForegroundColor White
Write-Host "2. Try the admin login again in Swagger UI" -ForegroundColor White
Write-Host "3. Use the /api/auth/debug endpoint to verify token contents" -ForegroundColor White

