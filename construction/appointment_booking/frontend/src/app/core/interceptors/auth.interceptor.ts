import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('🔒 Auth Interceptor - Request URL:', req.url);
    
    // Get token from localStorage
    const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;
    
    // Skip token for authentication endpoints
    if (req.url.includes('/auth/')) {
      console.log('🔒 Auth Interceptor - Skipping auth header for auth endpoint');
      return next.handle(req);
    }
    
    // Clone the request and add authorization header if token exists
    if (token) {
      console.log('🔒 Auth Interceptor - Adding authorization header');
      const authReq = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
          .set('Content-Type', 'application/json')
      });
      return next.handle(authReq);
    }
    
    // Add Content-Type header for requests without token
    const reqWithHeaders = req.clone({
      headers: req.headers.set('Content-Type', 'application/json')
    });
    
    console.log('🔒 Auth Interceptor - No token, proceeding without auth header');
    return next.handle(reqWithHeaders);
  }
}
