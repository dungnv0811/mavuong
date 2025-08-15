# 🔧 Debugging Steps for Login Issue

## Issues Fixed

### ✅ 1. CORS Configuration
**Problem**: Backend CORS didn't include `http://localhost:8000`
**Fixed**: Added `http://localhost:8000` to allowed origins in `WebConfig.java`

### ✅ 2. Proxy Configuration  
**Problem**: Proxy was using `secure: true` 
**Fixed**: Changed to `secure: false` in `proxy.conf.json`

### ✅ 3. Enhanced Logging
**Added**: Detailed console logging in `AuthService` to track login attempts

## 🧪 Testing Steps

### Step 1: Check Backend Status
```bash
# Check if backend is running
curl http://localhost:8090/api/v1/doctors
# Should return 403 (this is expected - auth required)
```

### Step 2: Test Direct Backend Login
```bash
# Test backend login directly
curl -X POST http://localhost:8090/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"emailOrUsername": "patient1", "password": "password"}'
```

### Step 3: Check Frontend Logs
1. Open browser DevTools → Console tab
2. Try logging in with `patient1` / `password`
3. Look for these console messages:
   - `🔐 Attempting login with URL: /api/v1/auth/login`
   - `🔐 Login payload: {emailOrUsername: "patient1", password: "password"}`
   - Either: `🔐 Backend login response:` OR `🔐 Backend authentication failed:`

### Step 4: Check Network Tab
1. Open DevTools → Network tab
2. Try logging in
3. Look for:
   - **Request URL**: Should be `/api/v1/auth/login` 
   - **Request Method**: POST
   - **Status**: Should be 200 or specific error code
   - **Response**: Check if there's a response body

## 🔍 Expected Behavior

### ✅ When Everything Works:
- Console shows: `🔐 Attempting login with URL: /api/v1/auth/login`
- Network tab shows: POST `/api/v1/auth/login` → 200 OK
- Console shows: `🔐 Backend login response:` with user data
- Login succeeds and redirects to dashboard

### ⚠️ When Backend is Down:
- Console shows: `🔐 Backend authentication failed:`
- Console shows: `🔐 Falling back to mock authentication`
- Console shows: `🔐 Mock login successful:` with mock user data
- Login succeeds with mock data

### ❌ When Backend Rejects Login:
- Console shows: `🔐 Backend authentication failed:` with error details
- Network tab shows: POST `/api/v1/auth/login` → 400/401/403
- Falls back to mock authentication

## 🚨 Troubleshooting

### Issue: Network tab shows `/login` instead of `/api/v1/auth/login`
**Cause**: Proxy not working
**Solution**: 
1. Restart frontend: `npm start`
2. Check proxy.conf.json is correct
3. Verify frontend is running with proxy

### Issue: CORS error in console
**Cause**: Backend doesn't allow localhost:8000
**Solution**: 
1. Restart backend after WebConfig changes
2. Check CORS configuration includes `http://localhost:8000`

### Issue: 403 Forbidden
**Cause**: Endpoint requires authentication OR CORS issue
**Solution**:
1. Check if endpoint should be public (auth endpoints should be)
2. Verify CORS headers in response

### Issue: Connection refused
**Cause**: Backend not running
**Solution**: Start backend with `mvn spring-boot:run`

## 🎯 Next Steps

1. **Start both services**:
   ```bash
   # Terminal 1: Backend
   cd construction/appointment_booking
   mvn spring-boot:run
   
   # Terminal 2: Frontend  
   cd construction/appointment_booking/frontend
   npm start
   ```

2. **Open browser**: `http://localhost:8000/login`

3. **Try login**: `patient1` / `password`

4. **Check console logs** for the 🔐 messages

5. **Check Network tab** for the actual HTTP requests

6. **Report findings**:
   - What URL is shown in Network tab?
   - What console messages appear?
   - What error response (if any)?

The enhanced logging will help us pinpoint exactly where the issue is occurring! 🎯
