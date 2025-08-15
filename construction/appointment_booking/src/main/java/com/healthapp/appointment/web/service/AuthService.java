package com.healthapp.appointment.web.service;

import com.healthapp.appointment.application.dto.UserDto;
import com.healthapp.appointment.domain.model.user.UserRole;
import com.healthapp.appointment.web.controller.AuthController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuthService {
    
    private final Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // In-memory user store (replace with database in production)
    private final Map<String, UserRecord> users = new HashMap<>();
    
    public AuthService() {
        // Initialize with demo users
        initializeDemoUsers();
    }
    
    private void initializeDemoUsers() {
        // Demo patient
        UserRecord patient = new UserRecord(
            UUID.randomUUID().toString(),
            "patient1",
            "John",
            "Doe", 
            "patient1@test.com",
            "+1234567890",
            LocalDate.of(1990, 1, 1),
            "123 Main St",
            "PATIENT",
            passwordEncoder.encode("password")
        );
        users.put("patient1", patient);
        users.put("patient1@test.com", patient);
        
        // Demo doctor
        UserRecord doctor = new UserRecord(
            UUID.randomUUID().toString(),
            "doctor1",
            "Dr. Jane",
            "Smith",
            "doctor1@test.com", 
            "+1234567891",
            LocalDate.of(1980, 5, 15),
            "456 Medical Ave",
            "DOCTOR",
            passwordEncoder.encode("password")
        );
        users.put("doctor1", doctor);
        users.put("doctor1@test.com", doctor);
    }
    
    public String authenticate(String username, String password) {
        UserRecord user = users.get(username);
        if (user == null || !passwordEncoder.matches(password, user.password())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        return generateToken(username, user.userRole());
    }
    
    public AuthController.AuthResult authenticateWithUserInfo(String emailOrUsername, String password) {
        UserRecord user = users.get(emailOrUsername);
        if (user == null || !passwordEncoder.matches(password, user.password())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        String token = generateToken(emailOrUsername, user.userRole());
        UserDto userDto = new UserDto(
            user.uuid(),
            user.username(),
            user.firstName(),
            user.lastName(),
            user.email(),
            user.phone(),
            user.dob(),
            user.address(),
            UserRole.valueOf(user.userRole())
        );
        
        return new AuthController.AuthResult(userDto, token);
    }
    
    public AuthController.AuthResult registerWithFullProfile(AuthController.SignupRequest request) {
        if (users.containsKey(request.username()) || users.containsKey(request.email())) {
            throw new RuntimeException("User already exists");
        }
        
        String uuid = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(request.password());
        
        UserRecord userRecord = new UserRecord(
            uuid,
            request.username(),
            request.firstName(),
            request.lastName(),
            request.email(),
            request.phone(),
            request.dob(),
            request.address(),
            request.userRole(),
            encodedPassword
        );
        
        users.put(request.username(), userRecord);
        users.put(request.email(), userRecord);
        
        String token = generateToken(request.username(), request.userRole());
        UserDto userDto = new UserDto(
            uuid,
            request.username(),
            request.firstName(),
            request.lastName(),
            request.email(),
            request.phone(),
            request.dob(),
            request.address(),
            UserRole.valueOf(request.userRole())
        );
        
        return new AuthController.AuthResult(userDto, token);
    }
    
    public void register(String username, String password, String role) {
        if (users.containsKey(username)) {
            throw new RuntimeException("User already exists");
        }
        
        String encodedPassword = passwordEncoder.encode(password);
        UserRecord user = new UserRecord(
            UUID.randomUUID().toString(),
            username,
            "First",
            "Last",
            username + "@test.com",
            "+1234567890",
            LocalDate.of(1990, 1, 1),
            "123 Test St",
            role,
            encodedPassword
        );
        users.put(username, user);
    }
    
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400000); // 24 hours
        
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtKey)
                .compact();
    }
    
    public String getUserFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }
    
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.get("role", String.class);
    }
    
    public String getUserRole(String username) {
        UserRecord user = users.get(username);
        return user != null ? user.userRole() : null;
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Key getJwtKey() {
        return jwtKey;
    }
    
    public record UserRecord(
        String uuid,
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
}
