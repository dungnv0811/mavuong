package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.application.dto.AppointmentDto;
import com.healthapp.appointment.application.service.AppointmentApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Appointments", description = "Appointment management operations")
@SecurityRequirement(name = "bearer-key")
public class AppointmentController {
    private final AppointmentApplicationService appointmentService;
    
    public AppointmentController(AppointmentApplicationService appointmentService) {
        this.appointmentService = appointmentService;
    }
    
    @PostMapping
    @Operation(summary = "Book a new appointment", description = "Allows patients to book appointments with doctors")
    public AppointmentDto bookAppointment(@RequestBody BookAppointmentRequest request) {
        return appointmentService.bookAppointment(
            request.patientId(),
            request.doctorId(),
            request.appointmentDateTime(),
            request.notes()
        );
    }
    
    @GetMapping
    @Operation(summary = "Get appointments", description = "Retrieve appointments by patient ID or doctor ID")
    public List<AppointmentDto> getAppointments(@RequestParam(required = false) Long patientId,
                                              @RequestParam(required = false) Long doctorId) {
        if (patientId != null) {
            return appointmentService.getPatientAppointments(patientId);
        }
        if (doctorId != null) {
            return appointmentService.getDoctorAppointments(doctorId);
        }
        throw new IllegalArgumentException("Either patientId or doctorId must be provided");
    }
    
    @PostMapping("/{id}/confirm")
    public AppointmentDto confirmAppointment(@PathVariable Long id) {
        return appointmentService.confirmAppointment(id);
    }

    @DeleteMapping("/{id}")
    public AppointmentDto cancelAppointment(@PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }

    @PutMapping("/{id}")
    public AppointmentDto rescheduleAppointment(@PathVariable Long id, @RequestBody RescheduleRequest request) {
        return appointmentService.rescheduleAppointment(id, request.newDateTime());
    }

    @GetMapping("/{id}")
    public AppointmentDto getById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }
    
    public record BookAppointmentRequest(
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentDateTime,
        String notes
    ) {}

    public record RescheduleRequest(LocalDateTime newDateTime) {}
}