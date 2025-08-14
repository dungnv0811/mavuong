package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.application.dto.AppointmentDto;
import com.healthapp.appointment.application.service.AppointmentApplicationService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {
    private final AppointmentApplicationService appointmentService;
    
    public AppointmentController(AppointmentApplicationService appointmentService) {
        this.appointmentService = appointmentService;
    }
    
    @PostMapping
    public AppointmentDto bookAppointment(@RequestBody BookAppointmentRequest request) {
        return appointmentService.bookAppointment(
            request.patientId(),
            request.doctorId(),
            request.appointmentDateTime(),
            request.notes()
        );
    }
    
    @GetMapping
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
    
    public record BookAppointmentRequest(
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentDateTime,
        String notes
    ) {}
}