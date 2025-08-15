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
        
        // All doctors from the list
        createDoctorAccount("elena.rodriguez", "Dr. Elena", "Rodriguez", "elena.rodriguez@hospital.com", "(555) 001-0001");
        createDoctorAccount("michael.chen", "Dr. Michael", "Chen", "michael.chen@hospital.com", "(555) 002-0002");
        createDoctorAccount("sarah.kim", "Dr. Sarah", "Kim", "sarah.kim@hospital.com", "(555) 003-0003");
        createDoctorAccount("david.green", "Dr. David", "Green", "david.green@hospital.com", "(555) 004-0004");
        createDoctorAccount("emily.white", "Dr. Emily", "White", "emily.white@hospital.com", "(555) 005-0005");
        createDoctorAccount("james.brown", "Dr. James", "Brown", "james.brown@hospital.com", "(555) 006-0006");
        createDoctorAccount("olivia.perez", "Dr. Olivia", "Perez", "olivia.perez@hospital.com", "(555) 007-0007");
        createDoctorAccount("ben.carter", "Dr. Ben", "Carter", "ben.carter@hospital.com", "(555) 008-0008");
        createDoctorAccount("sofia.garcia", "Dr. Sofia", "Garcia", "sofia.garcia@hospital.com", "(555) 009-0009");
        createDoctorAccount("john.smith", "Dr. John", "Smith", "john.smith@hospital.com", "555-0101");
        createDoctorAccount("sarah.johnson", "Dr. Sarah", "Johnson", "sarah.johnson@hospital.com", "555-0102");
        createDoctorAccount("mike.wilson", "Dr. Mike", "Wilson", "mike.wilson@hospital.com", "555-0103");
        createDoctorAccount("lisa.brown", "Dr. Lisa", "Brown", "lisa.brown@hospital.com", "555-0104");
        createDoctorAccount("david.lee", "Dr. David", "Lee", "david.lee@hospital.com", "555-0105");
    }
    
    private void createDoctorAccount(String username, String firstName, String lastName, String email, String phone) {
        UserRecord doctor = new UserRecord(
            UUID.randomUUID().toString(),
            username,
            firstName,
            lastName,
            email,
            phone,
            LocalDate.of(1975, 1, 1), // Default DOB
            "Medical Center", // Default address
            "DOCTOR",
            passwordEncoder.encode("password")
        );
        users.put(username, doctor);
        users.put(email, doctor);
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
