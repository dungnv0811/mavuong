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
    public ResponseEntity<PaginatedAppointmentsResponse> getUpcomingAppointments(
            @RequestParam String userID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            System.out.println("📅 AppointmentController: Getting upcoming appointments for user: " + userID);
            List<AppointmentDto> allAppointments = appointmentService.getUpcomingAppointments(userID);
            
            // Apply pagination
            int totalElements = allAppointments.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalElements);
            
            List<AppointmentDto> paginatedAppointments = startIndex < totalElements ? 
                allAppointments.subList(startIndex, endIndex) : List.of();
            
            PaginatedAppointmentsResponse response = new PaginatedAppointmentsResponse(
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
    public ResponseEntity<PaginatedAppointmentsResponse> getAppointmentHistory(
            @RequestParam String userID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            System.out.println("📅 AppointmentController: Getting appointment history for user: " + userID);
            List<AppointmentDto> allAppointments = appointmentService.getAppointmentHistory(userID);
            
            // Apply pagination
            int totalElements = allAppointments.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalElements);
            
            List<AppointmentDto> paginatedAppointments = startIndex < totalElements ? 
                allAppointments.subList(startIndex, endIndex) : List.of();
            
            PaginatedAppointmentsResponse response = new PaginatedAppointmentsResponse(
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
}