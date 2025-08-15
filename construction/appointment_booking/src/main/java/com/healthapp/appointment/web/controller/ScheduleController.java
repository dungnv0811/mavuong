package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.application.dto.DoctorScheduleDto;
import com.healthapp.appointment.application.service.ScheduleApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Schedules", description = "Doctor schedule and time slot management")
public class ScheduleController {
    
    private final ScheduleApplicationService scheduleService;
    
    public ScheduleController(ScheduleApplicationService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    @GetMapping("/{doctorID}")
    @Operation(summary = "Get doctor schedule", description = "Get doctor's schedule for specific date")
    public ResponseEntity<ScheduleResponse> getDoctorSchedule(
            @PathVariable String doctorID,
            @RequestParam String date) {
        try {
            DoctorScheduleDto schedule = scheduleService.getDoctorSchedule(doctorID, date);
            return ResponseEntity.ok(new ScheduleResponse(schedule));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{doctorID}/book")
    @Operation(summary = "Book time slot", description = "Book a time slot for appointment")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<BookingResponse> bookTimeSlot(
            @PathVariable String doctorID,
            @RequestBody BookTimeSlotRequest request) {
        try {
            boolean success = scheduleService.bookTimeSlot(doctorID, request.date(), request.time());
            return ResponseEntity.ok(new BookingResponse(success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BookingResponse(false));
        }
    }
    
    @PostMapping("/{doctorID}/release")
    @Operation(summary = "Release time slot", description = "Release a booked time slot")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<BookingResponse> releaseTimeSlot(
            @PathVariable String doctorID,
            @RequestBody ReleaseTimeSlotRequest request) {
        try {
            boolean success = scheduleService.releaseTimeSlot(doctorID, request.date(), request.time());
            return ResponseEntity.ok(new BookingResponse(success));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BookingResponse(false));
        }
    }
    
    public record ScheduleResponse(DoctorScheduleDto schedule) {}
    public record BookingResponse(boolean success) {}
    public record BookTimeSlotRequest(String date, String time) {}
    public record ReleaseTimeSlotRequest(String date, String time) {}
}
