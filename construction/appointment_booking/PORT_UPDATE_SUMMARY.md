# Backend Port Update Summary

## Changes Made

### Backend Changes
✅ **Updated Spring Boot port from 8080 to 8090**
- Modified `application.yml`: `server.port: 8090`
- Backend now runs on `http://localhost:8090/api/v1`

### Frontend Changes Required and Completed
✅ **Created Environment Configuration**
- Added `src/environments/environment.ts` (development)
- Added `src/environments/environment.prod.ts` (production)
- Both pointing to `http://localhost:8090/api/v1`

✅ **Added Proxy Configuration**
- Created `proxy.conf.json` to redirect `/api/*` to `http://localhost:8090`
- Updated `package.json` scripts to use proxy by default

✅ **Updated Angular Configuration**
- Modified `angular.json` to use environment files
- Added file replacements for production builds

✅ **Updated API Service**
- Modified `api.service.ts` to use environment configuration
- Uses proxy in development, direct URL in production

## How to Use

### Development Mode (Recommended)
```bash
# Start backend (runs on port 8090)
cd construction/appointment_booking
mvn spring-boot:run

# Start frontend with proxy (runs on port 4200, proxies API calls to 8090)
cd construction/appointment_booking/frontend  
npm start
```

### Alternative: No Proxy Mode
```bash
# If you need to run frontend without proxy
npm run start:no-proxy
```

## Configuration Details

### Environment Files
- **Development**: Uses proxy configuration (`/api` → `http://localhost:8090`)
- **Production**: Uses direct URL (`http://localhost:8090/api/v1`)

### Proxy Configuration (`proxy.conf.json`)
```json
{
  "/api/*": {
    "target": "http://localhost:8090",
    "secure": true,
    "changeOrigin": true,
    "logLevel": "debug"
  }
}
```

### Updated Scripts (`package.json`)
- `npm start` → runs with proxy (default)
- `npm run start:no-proxy` → runs without proxy

## Testing

### Backend API (Direct)
```bash
curl -X GET http://localhost:8090/api/v1/doctors
```

### Frontend API (Via Proxy)
When frontend runs on `http://localhost:4200`:
- Frontend makes call to `/api/doctors`
- Proxy redirects to `http://localhost:8090/api/v1/doctors`

## Benefits of This Setup

1. **No CORS Issues**: Proxy handles cross-origin requests
2. **Environment Flexibility**: Easy to switch between dev/prod configurations
3. **Clean Frontend Code**: Frontend uses relative URLs (`/api/...`)
4. **Production Ready**: Direct API URLs for production builds

## Troubleshooting

### If proxy doesn't work:
1. Ensure backend is running on port 8090
2. Check `proxy.conf.json` configuration
3. Restart frontend after proxy configuration changes
4. Use browser dev tools to check network requests

### If API calls fail:
1. Verify backend is running: `curl http://localhost:8090/api/v1/doctors`
2. Check browser console for errors
3. Verify environment configuration is correct
