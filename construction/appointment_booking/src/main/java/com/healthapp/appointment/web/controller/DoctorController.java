package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.application.dto.DoctorDto;
import com.healthapp.appointment.application.service.DoctorApplicationService;
import com.healthapp.appointment.domain.model.doctor.DoctorSchedule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Doctors", description = "Doctor information and availability operations")
public class DoctorController {
    private final DoctorApplicationService doctorService;
    
    public DoctorController(DoctorApplicationService doctorService) {
        this.doctorService = doctorService;
    }
    
    @GetMapping
    @Operation(summary = "Get all doctors", description = "Retrieve all doctors or filter by specialty (public endpoint)")
    public List<DoctorDto> getAllDoctors(@RequestParam(required = false) String specialty) {
        if (specialty != null) {
            return doctorService.getDoctorsBySpecialty(specialty);
        }
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}/availability")
    public List<String> getDoctorAvailability(@PathVariable Long id) {
        return doctorService.getDoctorAvailability(id);
    }

    @GetMapping("/{id}")
    public DoctorDto getDoctor(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @PostMapping("/{id}/schedule")
    @Operation(summary = "Update doctor schedule", description = "Update or create doctor availability schedule (requires authentication)")
    @SecurityRequirement(name = "bearer-key")
    public DoctorSchedule upsertSchedule(@PathVariable Long id, @RequestBody DoctorSchedule schedule) {
        return doctorService.saveSchedule(new DoctorSchedule(
            id,
            schedule.getDayOfWeek(),
            schedule.getStartTime(),
            schedule.getEndTime()
        ));
    }
}