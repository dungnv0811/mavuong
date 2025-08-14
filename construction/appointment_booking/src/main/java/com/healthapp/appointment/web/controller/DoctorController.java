package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.application.dto.DoctorDto;
import com.healthapp.appointment.application.service.DoctorApplicationService;
import com.healthapp.appointment.domain.model.doctor.DoctorSchedule;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {
    private final DoctorApplicationService doctorService;
    
    public DoctorController(DoctorApplicationService doctorService) {
        this.doctorService = doctorService;
    }
    
    @GetMapping
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
    public DoctorSchedule upsertSchedule(@PathVariable Long id, @RequestBody DoctorSchedule schedule) {
        return doctorService.saveSchedule(new DoctorSchedule(
            id,
            schedule.getDayOfWeek(),
            schedule.getStartTime(),
            schedule.getEndTime()
        ));
    }
}