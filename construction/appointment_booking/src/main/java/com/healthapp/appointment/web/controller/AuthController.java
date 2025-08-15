package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.application.dto.UserDto;
import com.healthapp.appointment.web.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Authentication", description = "User authentication and authorization operations")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with email/username and password to get JWT token")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResult result = authService.authenticateWithUserInfo(request.emailOrUsername(), request.password());
            return ResponseEntity.ok(new LoginResponse(result.user(), result.token()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/signup")
    @Operation(summary = "User registration", description = "Register a new user with complete profile information")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        try {
            AuthResult result = authService.registerWithFullProfile(request);
            return ResponseEntity.ok(new SignupResponse(result.user(), result.token()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/google")
    @Operation(summary = "Google OAuth login", description = "Authenticate using Google OAuth token")
    public ResponseEntity<GoogleLoginResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
        try {
            // For now, return placeholder - implement Google OAuth later
            return ResponseEntity.ok(new GoogleLoginResponse(null, "Google login not implemented yet"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/user")
    @Operation(summary = "Get current user", description = "Get current authenticated user information")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<UserResponse> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String cleanToken = token.replace("Bearer ", "");
            String username = authService.getUserFromToken(cleanToken);
            String role = authService.getUserRole(username);
            return ResponseEntity.ok(new UserResponse(username, role));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponse(null, null));
        }
    }

    @GetMapping("/debug")
    @Operation(summary = "Debug token information", description = "Debug endpoint to check token and role extraction")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<DebugResponse> debugToken(@RequestHeader("Authorization") String token) {
        try {
            String cleanToken = token.replace("Bearer ", "");
            String username = authService.getUserFromToken(cleanToken);
            String roleFromToken = authService.getRoleFromToken(cleanToken);
            String roleFromUser = authService.getUserRole(username);
            boolean isValid = authService.validateToken(cleanToken);
            
            return ResponseEntity.ok(new DebugResponse(
                username, 
                roleFromToken, 
                roleFromUser, 
                isValid,
                cleanToken.substring(0, Math.min(20, cleanToken.length())) + "..."
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new DebugResponse(
                null, null, null, false, "Error: " + e.getMessage()
            ));
        }
    }
    
    // Request/Response records for frontend compatibility
    public record LoginRequest(String emailOrUsername, String password) {}
    public record LoginResponse(UserDto user, String token) {}
    
    public record SignupRequest(
        String username,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate dob,
        String address,
        String userRole,
        String password
    ) {}
    public record SignupResponse(UserDto user, String token) {}
    
    public record GoogleLoginRequest(String googleToken) {}
    public record GoogleLoginResponse(UserDto user, String token) {}
    
    public record UserResponse(String username, String role) {}
    public record DebugResponse(String username, String roleFromToken, String roleFromUser, boolean tokenValid, String tokenPreview) {}
    
    public record AuthResult(UserDto user, String token) {}
}
