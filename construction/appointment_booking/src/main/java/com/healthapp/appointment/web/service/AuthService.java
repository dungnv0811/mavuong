package com.healthapp.appointment.web.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class AuthService {
    
    private final Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // In-memory user store (replace with database in production)
    private final Map<String, User> users = new HashMap<>();
    
    public AuthService() {
        // Initialize with demo users
        register("patient1", "password", "PATIENT");
        register("doctor1", "password", "DOCTOR");
        register("admin", "password", "ADMIN");
    }
    
    public String authenticate(String username, String password) {
        User user = users.get(username);
        if (user == null || !passwordEncoder.matches(password, user.password())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        return generateToken(username, user.role());
    }
    
    public void register(String username, String password, String role) {
        if (users.containsKey(username)) {
            throw new RuntimeException("User already exists");
        }
        
        String encodedPassword = passwordEncoder.encode(password);
        users.put(username, new User(username, encodedPassword, role));
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
        User user = users.get(username);
        return user != null ? user.role() : null;
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
    
    public record User(String username, String password, String role) {}
}
