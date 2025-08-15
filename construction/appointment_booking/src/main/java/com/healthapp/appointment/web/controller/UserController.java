package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.application.dto.UserDto;
import com.healthapp.appointment.application.service.UserApplicationService;
import com.healthapp.appointment.domain.model.user.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Users", description = "User management operations")
@SecurityRequirement(name = "bearer-key")
public class UserController {
    
    private final UserApplicationService userService;
    
    public UserController(UserApplicationService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/{userUuid}")
    @Operation(summary = "Get user by UUID", description = "Retrieve user information by UUID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String userUuid) {
        try {
            UserDto user = userService.getUserByUuid(userUuid);
            return ResponseEntity.ok(new UserResponse(user));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search user", description = "Search user by email, username, or full name")
    public ResponseEntity<UserResponse> searchUser(@RequestParam String query) {
        try {
            UserDto user = userService.searchUser(query);
            return ResponseEntity.ok(new UserResponse(user));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{userUuid}")
    @Operation(summary = "Update user", description = "Update user information")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String userUuid, 
            @RequestBody UserDto userDto) {
        try {
            UserDto updatedUser = userService.updateUser(userUuid, userDto);
            return ResponseEntity.ok(new UserResponse(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{userUuid}/profile")
    @Operation(summary = "Update user profile", description = "Update user profile information")
    public ResponseEntity<UpdateProfileResponse> updateUserProfile(
            @PathVariable String userUuid,
            @RequestBody UpdateProfileRequest request) {
        try {
            System.out.println("👤 UserController: Updating profile for user: " + userUuid);
            
            // For now, just return success with the updated data
            // In a real implementation, this would update the database
            
            // Parse the date string to LocalDate
            LocalDate dobDate = null;
            try {
                if (request.dob() != null && !request.dob().isEmpty()) {
                    dobDate = LocalDate.parse(request.dob());
                }
            } catch (Exception e) {
                // If parsing fails, set to null
                dobDate = null;
            }
            
            UserDto updatedUser = new UserDto(
                userUuid,
                request.username(),
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone(),
                dobDate,
                request.address(),
                UserRole.PATIENT // Use enum instead of string
            );
            
            return ResponseEntity.ok(new UpdateProfileResponse(updatedUser));
        } catch (Exception e) {
            System.err.println("👤 UserController: Error updating profile: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    public record UserResponse(UserDto user) {}
    public record UpdateProfileRequest(
        String username,
        String firstName,
        String lastName,
        String email,
        String phone,
        String dob,
        String address
    ) {}
    public record UpdateProfileResponse(UserDto user) {}
}
