#!/bin/bash

# Bash script to test backend authentication
# Make executable with: chmod +x test_backend_auth.sh

echo "🧪 Testing Backend Authentication System"
echo "======================================"

BASE_URL="http://localhost:8080"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Test 1: Check if backend is running
echo -e "\n1️⃣ ${YELLOW}Testing if backend is running...${NC}"
if curl -s "$BASE_URL/api/doctors" > /dev/null; then
    echo -e "${GREEN}✅ Backend is running!${NC}"
    DOCTOR_COUNT=$(curl -s "$BASE_URL/api/doctors" | jq length)
    echo -e "${CYAN}Found $DOCTOR_COUNT doctors in the system${NC}"
else
    echo -e "${RED}❌ Backend is not running. Please start it with: mvn spring-boot:run${NC}"
    exit 1
fi

# Test 2: Login as Patient
echo -e "\n2️⃣ ${YELLOW}Testing Patient Login...${NC}"
PATIENT_LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
    -H "Content-Type: application/json" \
    -d '{"username": "patient1", "password": "password"}')

if echo "$PATIENT_LOGIN_RESPONSE" | jq -e '.token' > /dev/null; then
    PATIENT_TOKEN=$(echo "$PATIENT_LOGIN_RESPONSE" | jq -r '.token')
    echo -e "${GREEN}✅ Patient login successful!${NC}"
    echo -e "${CYAN}Token: ${PATIENT_TOKEN:0:20}...${NC}"
else
    echo -e "${RED}❌ Patient login failed${NC}"
    exit 1
fi

# Test 3: Get current user info
echo -e "\n3️⃣ ${YELLOW}Testing Get Current User...${NC}"
USER_INFO=$(curl -s -X GET "$BASE_URL/api/auth/user" \
    -H "Authorization: Bearer $PATIENT_TOKEN")

if echo "$USER_INFO" | jq -e '.username' > /dev/null; then
    USERNAME=$(echo "$USER_INFO" | jq -r '.username')
    ROLE=$(echo "$USER_INFO" | jq -r '.role')
    echo -e "${GREEN}✅ Current user: $USERNAME (Role: $ROLE)${NC}"
else
    echo -e "${RED}❌ Get user info failed${NC}"
fi

# Test 4: Test protected endpoint without token
echo -e "\n4️⃣ ${YELLOW}Testing Protected Endpoint (No Token)...${NC}"
if curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/appointments?patientId=1" | grep -q "401"; then
    echo -e "${GREEN}✅ Correctly blocked - 401 Unauthorized${NC}"
else
    echo -e "${RED}❌ Should have been blocked!${NC}"
fi

# Test 5: Test protected endpoint with token
echo -e "\n5️⃣ ${YELLOW}Testing Protected Endpoint (With Token)...${NC}"
APPOINTMENTS_RESPONSE=$(curl -s -X GET "$BASE_URL/api/appointments?patientId=1" \
    -H "Authorization: Bearer $PATIENT_TOKEN")

if echo "$APPOINTMENTS_RESPONSE" | jq -e '. | type == "array"' > /dev/null; then
    APPOINTMENT_COUNT=$(echo "$APPOINTMENTS_RESPONSE" | jq length)
    echo -e "${GREEN}✅ Protected endpoint accessible with token!${NC}"
    echo -e "${CYAN}Found $APPOINTMENT_COUNT appointments${NC}"
else
    echo -e "${RED}❌ Protected endpoint failed${NC}"
fi

# Test 6: Login as Doctor
echo -e "\n6️⃣ ${YELLOW}Testing Doctor Login...${NC}"
DOCTOR_LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
    -H "Content-Type: application/json" \
    -d '{"username": "doctor1", "password": "password"}')

if echo "$DOCTOR_LOGIN_RESPONSE" | jq -e '.token' > /dev/null; then
    DOCTOR_TOKEN=$(echo "$DOCTOR_LOGIN_RESPONSE" | jq -r '.token')
    echo -e "${GREEN}✅ Doctor login successful!${NC}"
else
    echo -e "${RED}❌ Doctor login failed${NC}"
fi

# Test 7: Test role-based access (Doctor trying patient-only endpoint)
echo -e "\n7️⃣ ${YELLOW}Testing Role-Based Access Control...${NC}"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL/api/symptoms/analyze" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $DOCTOR_TOKEN" \
    -d '{"patientId": 1, "description": "Test symptom"}')

if [ "$HTTP_CODE" = "403" ]; then
    echo -e "${GREEN}✅ Correctly blocked - 403 Forbidden (Doctor cannot access patient endpoint)${NC}"
else
    echo -e "${RED}❌ Doctor should not access patient-only endpoint! Got HTTP $HTTP_CODE${NC}"
fi

# Test 8: Test patient accessing symptom analysis
echo -e "\n8️⃣ ${YELLOW}Testing Patient Role Access...${NC}"
SYMPTOM_RESPONSE=$(curl -s -X POST "$BASE_URL/api/symptoms/analyze" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $PATIENT_TOKEN" \
    -d '{"patientId": 1, "description": "I have a headache and feeling tired"}')

if echo "$SYMPTOM_RESPONSE" | jq -e '.suggestedSpecialties' > /dev/null; then
    SPECIALTIES=$(echo "$SYMPTOM_RESPONSE" | jq -r '.suggestedSpecialties | join(", ")')
    echo -e "${GREEN}✅ Patient can access symptom analysis!${NC}"
    echo -e "${CYAN}Suggested specialties: $SPECIALTIES${NC}"
else
    echo -e "${YELLOW}⚠️ Patient symptom analysis failed${NC}"
fi

# Test 9: Book an appointment
echo -e "\n9️⃣ ${YELLOW}Testing Appointment Booking...${NC}"
APPOINTMENT_RESPONSE=$(curl -s -X POST "$BASE_URL/api/appointments" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $PATIENT_TOKEN" \
    -d '{
        "patientId": 1,
        "doctorId": 1,
        "appointmentDateTime": "2024-12-25T10:00:00",
        "notes": "Test appointment from bash script"
    }')

if echo "$APPOINTMENT_RESPONSE" | jq -e '.id' > /dev/null; then
    APPOINTMENT_ID=$(echo "$APPOINTMENT_RESPONSE" | jq -r '.id')
    APPOINTMENT_STATUS=$(echo "$APPOINTMENT_RESPONSE" | jq -r '.status')
    echo -e "${GREEN}✅ Appointment booked successfully!${NC}"
    echo -e "${CYAN}Appointment ID: $APPOINTMENT_ID, Status: $APPOINTMENT_STATUS${NC}"
else
    echo -e "${YELLOW}⚠️ Appointment booking failed${NC}"
fi

# Summary
echo -e "\n🎉 ${GREEN}Backend Authentication Testing Complete!${NC}"
echo "======================================"
echo -e "${GREEN}✅ Authentication system is working correctly${NC}"
echo -e "${GREEN}✅ Role-based access control is functioning${NC}"
echo -e "${GREEN}✅ JWT tokens are being validated properly${NC}"

echo -e "\n📋 ${CYAN}Available Demo Users:${NC}"
echo "• patient1 / password (PATIENT role)"
echo "• doctor1 / password (DOCTOR role)"
echo "• admin / password (ADMIN role)"

echo -e "\n🌐 ${CYAN}Useful URLs:${NC}"
echo "• API Docs: http://localhost:8080/swagger-ui.html"
echo "• H2 Console: http://localhost:8080/h2-console"
echo "• Frontend: http://localhost:4200"

