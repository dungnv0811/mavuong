package com.healthapp.appointment.application.service;

import com.healthapp.appointment.application.dto.UserDto;
import com.healthapp.appointment.domain.model.user.UserRole;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserApplicationService {
    
    // In-memory store for demo purposes
    private final Map<String, UserDto> usersByUuid = new HashMap<>();
    private final Map<String, UserDto> usersBySearchKey = new HashMap<>();
    
    public UserApplicationService() {
        initializeDemoUsers();
    }
    
    private void initializeDemoUsers() {
        UserDto patient = new UserDto(
            "patient-uuid-123",
            "patient1",
            "John",
            "Doe",
            "patient1@test.com",
            "+1234567890",
            LocalDate.of(1990, 1, 1),
            "123 Main St",
            UserRole.PATIENT
        );
        
        UserDto doctor = new UserDto(
            "doctor-uuid-456",
            "doctor1",
            "Dr. Jane",
            "Smith",
            "doctor1@test.com",
            "+1234567891",
            LocalDate.of(1980, 5, 15),
            "456 Medical Ave",
            UserRole.DOCTOR
        );
        
        usersByUuid.put(patient.uuid(), patient);
        usersByUuid.put(doctor.uuid(), doctor);
        
        // Add search keys
        usersBySearchKey.put(patient.email(), patient);
        usersBySearchKey.put(patient.username(), patient);
        usersBySearchKey.put(patient.firstName() + " " + patient.lastName(), patient);
        
        usersBySearchKey.put(doctor.email(), doctor);
        usersBySearchKey.put(doctor.username(), doctor);
        usersBySearchKey.put(doctor.firstName() + " " + doctor.lastName(), doctor);
    }
    
    public UserDto getUserByUuid(String uuid) {
        UserDto user = usersByUuid.get(uuid);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }
    
    public UserDto searchUser(String query) {
        UserDto user = usersBySearchKey.get(query);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }
    
    public UserDto updateUser(String uuid, UserDto updatedUser) {
        UserDto existingUser = usersByUuid.get(uuid);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }
        
        UserDto newUser = new UserDto(
            uuid,
            updatedUser.username() != null ? updatedUser.username() : existingUser.username(),
            updatedUser.firstName() != null ? updatedUser.firstName() : existingUser.firstName(),
            updatedUser.lastName() != null ? updatedUser.lastName() : existingUser.lastName(),
            updatedUser.email() != null ? updatedUser.email() : existingUser.email(),
            updatedUser.phone() != null ? updatedUser.phone() : existingUser.phone(),
            updatedUser.dob() != null ? updatedUser.dob() : existingUser.dob(),
            updatedUser.address() != null ? updatedUser.address() : existingUser.address(),
            updatedUser.userRole() != null ? updatedUser.userRole() : existingUser.userRole()
        );
        
        usersByUuid.put(uuid, newUser);
        return newUser;
    }
}
