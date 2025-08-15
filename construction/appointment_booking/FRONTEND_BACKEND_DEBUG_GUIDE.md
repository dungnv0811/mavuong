# Frontend-Backend Connection Debug Guide

## 🚨 Issues Found and Fixed

### Issue 1: Mock Data Services (FIXED)
**Problem**: All frontend services were using mock data instead of making HTTP calls to the backend.

**Services Updated**:
- ✅ `DoctorBookingService` - Now calls `/api/v1/doctors` with fallback to mock data
- ✅ `AppointmentService` - Now calls `/api/v1/appointments/*` with fallback to mock data  
- ✅ `AuthService` - Now calls `/api/v1/auth/*` with fallback to mock authentication

### Issue 2: Proxy Configuration (FIXED)
**Problem**: Frontend runs on port 8000, but proxy was not properly configured.

**Fixed**:
- ✅ Updated `proxy.conf.json` to proxy `/api/v1/*` to `http://localhost:8090`
- ✅ Added JWT token interceptor for authenticated requests
- ✅ Services now use `/api/v1` base URL for API calls

## 🔧 How It Works Now

### API Call Flow:
1. **Frontend** (port 8000) makes call to `/api/v1/doctors`
2. **Proxy** redirects to `http://localhost:8090/api/v1/doctors`  
3. **Backend** (port 8090) processes request and returns data
4. **Frontend** receives real data or falls back to mock data on error

### Authentication Flow:
1. User logs in via `/api/v1/auth/login`
2. Backend returns JWT token + user data
3. Token stored in localStorage
4. AuthInterceptor adds `Authorization: Bearer <token>` to subsequent requests

## 🧪 Testing Steps

### 1. Start Backend
```bash
cd construction/appointment_booking
mvn spring-boot:run
# Backend runs on http://localhost:8090/api/v1
```

### 2. Start Frontend with Proxy  
```bash
cd construction/appointment_booking/frontend
npm start
# Frontend runs on http://localhost:8000 with proxy to backend
```

### 3. Test API Calls
Open browser DevTools Network tab and:

1. **Visit** `http://localhost:8000/appointment-booking`
2. **Login** with credentials: `patient1` / `password`
3. **Check Network Tab** for API calls:
   - Should see calls to `/api/v1/auth/login`
   - Should see calls to `/api/v1/doctors`
   - Should see calls to `/api/v1/appointments/upcoming`

### 4. Verify Backend Connection
```bash
# Test backend directly
curl -X GET http://localhost:8090/api/v1/doctors

# Test with authentication
curl -X POST http://localhost:8090/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"emailOrUsername": "patient1", "password": "password"}'
```

## 🔍 Debugging Tips

### Check Browser Console
Look for these messages:
- ✅ **Success**: No error messages, data loads normally
- ❌ **Fallback**: "Backend call failed, using mock data"
- ❌ **Error**: HTTP 4xx/5xx errors in Network tab

### Check Network Tab
Expected API calls:
- `GET /api/v1/doctors` → Returns doctor list
- `GET /api/v1/appointments/upcoming?userID=xxx` → Returns appointments
- `POST /api/v1/auth/login` → Returns user + token

### Common Issues

#### Issue: "ERR_CONNECTION_REFUSED"
**Cause**: Backend not running
**Solution**: Start backend with `mvn spring-boot:run`

#### Issue: "404 Not Found" 
**Cause**: API endpoint mismatch
**Solution**: Check backend has updated endpoints

#### Issue: "CORS Error"
**Cause**: CORS misconfiguration  
**Solution**: Verify backend CORS allows localhost:8000

#### Issue: Still seeing mock data
**Cause**: Proxy not working or backend down
**Solution**: 
1. Check proxy configuration
2. Restart frontend with `npm start`
3. Verify backend is running

## 🎯 Expected Behavior

### When Backend is Running:
- Real data loads from backend
- Login works with JWT tokens
- Network tab shows API calls to `/api/v1/*`
- Console shows no "fallback" warnings

### When Backend is Down:
- Mock data displays (graceful degradation)
- Console shows "Backend call failed, using mock data"
- App still works but with demo data

## 🛠️ Configuration Files Updated

1. **proxy.conf.json** - Proxy configuration for port 8000
2. **environment.ts** - API URL configuration  
3. **Services** - All services now make HTTP calls with fallbacks
4. **app.config.ts** - Added JWT interceptor
5. **auth.interceptor.ts** - Automatic token handling

## ✅ Verification Checklist

- [ ] Backend running on port 8090
- [ ] Frontend running on port 8000 with proxy
- [ ] Login works and returns JWT token
- [ ] Doctor listing loads from backend
- [ ] Appointments load from backend  
- [ ] Network tab shows API calls to `/api/v1/*`
- [ ] No CORS errors in console
- [ ] Graceful fallback to mock data when backend unavailable

Your frontend should now properly connect to the backend! 🎉
