package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.application.dto.AppointmentDto;
import com.healthapp.appointment.application.service.AppointmentApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Appointments", description = "Appointment management operations")
@SecurityRequirement(name = "bearer-key")
public class AppointmentController {
    private final AppointmentApplicationService appointmentService;
    
    public AppointmentController(AppointmentApplicationService appointmentService) {
        this.appointmentService = appointmentService;
    }
    
    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming appointments", description = "Get user's upcoming appointments with pagination")
    public ResponseEntity<EnrichedPaginatedAppointmentsResponse> getUpcomingAppointments(
            @RequestParam String userID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            System.out.println("📅 AppointmentController: Getting upcoming appointments for user: " + userID);
            List<AppointmentDto> allAppointments = appointmentService.getUpcomingAppointments(userID);
            
            // Enrich appointments with doctor information for frontend
            List<EnrichedAppointmentDto> enrichedAppointments = allAppointments.stream()
                .map(this::enrichAppointmentData)
                .toList();
            
            // Apply pagination
            int totalElements = enrichedAppointments.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalElements);
            
            List<EnrichedAppointmentDto> paginatedAppointments = startIndex < totalElements ? 
                enrichedAppointments.subList(startIndex, endIndex) : List.of();
            
            EnrichedPaginatedAppointmentsResponse response = new EnrichedPaginatedAppointmentsResponse(
                paginatedAppointments,
                page,
                size,
                totalElements,
                totalPages,
                page < totalPages - 1,
                page > 0
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("📅 AppointmentController: Error getting upcoming appointments: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/history")
    @Operation(summary = "Get appointment history", description = "Get user's appointment history with pagination")
    public ResponseEntity<EnrichedPaginatedAppointmentsResponse> getAppointmentHistory(
            @RequestParam String userID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            System.out.println("📅 AppointmentController: Getting appointment history for user: " + userID);
            List<AppointmentDto> allAppointments = appointmentService.getAppointmentHistory(userID);
            
            // Enrich appointments with doctor information for frontend
            List<EnrichedAppointmentDto> enrichedAppointments = allAppointments.stream()
                .map(this::enrichAppointmentData)
                .toList();
            
            // Apply pagination
            int totalElements = enrichedAppointments.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalElements);
            
            List<EnrichedAppointmentDto> paginatedAppointments = startIndex < totalElements ? 
                enrichedAppointments.subList(startIndex, endIndex) : List.of();
            
            EnrichedPaginatedAppointmentsResponse response = new EnrichedPaginatedAppointmentsResponse(
                paginatedAppointments,
                page,
                size,
                totalElements,
                totalPages,
                page < totalPages - 1,
                page > 0
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("📅 AppointmentController: Error getting appointment history: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    @Operation(summary = "Create new appointment", description = "Create new appointment with patient info")
    public ResponseEntity<CreateAppointmentResponse> createAppointment(@RequestBody CreateAppointmentRequest request) {
        try {
            AppointmentDto appointment = appointmentService.createAppointment(request);
            return ResponseEntity.ok(new CreateAppointmentResponse(appointment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{appointmentId}/cancel")
    @Operation(summary = "Cancel appointment", description = "Cancel appointment")
    public ResponseEntity<CancelAppointmentResponse> cancelAppointment(@PathVariable String appointmentId) {
        try {
            AppointmentDto appointment = appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity.ok(new CancelAppointmentResponse(appointment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Request/Response records for frontend compatibility
    public record CreateAppointmentRequest(
        String doctorID,
        String userID,
        String dateTime,
        String place,
        PatientInfo patientInfo
    ) {
        public record PatientInfo(
            String fullName,
            String email,
            String phone,
            String gender,
            String reason,
            String insurance,
            String policyNumber
        ) {}
    }
    
    public record UpcomingAppointmentsResponse(List<AppointmentDto> appointments) {}
    public record AppointmentHistoryResponse(List<AppointmentDto> appointments) {}
    public record PaginatedAppointmentsResponse(
        List<AppointmentDto> appointments,
        int page,
        int size,
        int totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
    ) {}
    public record CreateAppointmentResponse(AppointmentDto appointment) {}
    public record CancelAppointmentResponse(AppointmentDto appointment) {}
    
    // Enhanced DTOs for frontend
    public record EnrichedAppointmentDto(
        String uuid,
        String doctorID,
        String userID,
        String doctor,
        String specialty,
        String dateTime,
        String place,
        String status,
        String avatarLink
    ) {}
    
    public record EnrichedPaginatedAppointmentsResponse(
        List<EnrichedAppointmentDto> appointments,
        int page,
        int size,
        int totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
    ) {}
    
    // Helper method to enrich appointment data
    private EnrichedAppointmentDto enrichAppointmentData(AppointmentDto appointment) {
        // Map doctor IDs to names and specialties (simplified mapping)
        String doctorName = switch (appointment.doctorId().intValue()) {
            case 1 -> "Dr. Elena Rodriguez";
            case 2 -> "Dr. Michael Chen";
            case 3 -> "Dr. Sarah Kim";
            case 4 -> "Dr. David Green";
            case 5 -> "Dr. Emily White";
            case 6 -> "Dr. James Brown";
            case 7 -> "Dr. Olivia Perez";
            case 8 -> "Dr. Ben Carter";
            default -> "Doctor " + appointment.doctorId();
        };
        
        String specialty = switch (appointment.doctorId().intValue()) {
            case 1 -> "General Practice";
            case 2 -> "Cardiology";
            case 3 -> "Dermatology";
            case 4 -> "Cardiology";
            case 5 -> "Ophthalmology";
            case 6 -> "Orthopedics";
            case 7 -> "Neurology";
            case 8 -> "Psychiatry";
            default -> "General Practice";
        };
        
        String avatarLink = doctorName.contains("Elena") || doctorName.contains("Emily") || 
                           doctorName.contains("Sarah") || doctorName.contains("Olivia") ? 
                           "/assets/avatars/female-doctor.jpg" : "/assets/avatars/male-doctor.jpg";
        
        return new EnrichedAppointmentDto(
            "apt-" + appointment.id(),
            String.valueOf(appointment.doctorId()),
            String.valueOf(appointment.patientId()),
            doctorName,
            specialty,
            appointment.appointmentDateTime().toString(),
            "Hospital",
            appointment.status().toString(),
            avatarLink
        );
    }
}