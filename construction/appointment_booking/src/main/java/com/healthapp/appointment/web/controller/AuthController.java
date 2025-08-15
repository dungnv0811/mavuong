package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.web.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Authentication", description = "User authentication and authorization operations")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user with username and password to get JWT token")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.authenticate(request.username(), request.password());
            return ResponseEntity.ok(new LoginResponse(token, "Login successful", request.username()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(null, "Invalid credentials", null));
        }
    }
    
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Register a new user with username, password and role")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request.username(), request.password(), request.role());
            return ResponseEntity.ok(new RegisterResponse("User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RegisterResponse("Registration failed: " + e.getMessage()));
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
    
    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String token, String message, String username) {}
    public record RegisterRequest(String username, String password, String role) {}
    public record RegisterResponse(String message) {}
    public record UserResponse(String username, String role) {}
    public record DebugResponse(String username, String roleFromToken, String roleFromUser, boolean tokenValid, String tokenPreview) {}
}
