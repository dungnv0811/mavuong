package com.healthapp.appointment.application.service;

import com.healthapp.appointment.application.dto.DoctorDto;
import com.healthapp.appointment.domain.model.doctor.DoctorSchedule;
import com.healthapp.appointment.domain.model.doctor.Doctor;
import com.healthapp.appointment.domain.model.doctor.DoctorId;
import com.healthapp.appointment.domain.repository.DoctorRepository;
import com.healthapp.appointment.infrastructure.repository.JpaDoctorScheduleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorApplicationService {
    private final DoctorRepository doctorRepository;
    private final JpaDoctorScheduleRepository scheduleRepository;
    
    public DoctorApplicationService(DoctorRepository doctorRepository,
                                   JpaDoctorScheduleRepository scheduleRepository) {
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
    }
    
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
            .map(this::toDto)
            .toList();
    }
    
    public List<DoctorDto> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty).stream()
            .map(this::toDto)
            .toList();
    }

    public DoctorDto getDoctorById(Long id) {
        return doctorRepository.findById(new DoctorId(id))
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
    
    public List<String> getDoctorAvailability(Long doctorId) {
        return scheduleRepository.findByDoctorId(doctorId).stream()
            .map(this::formatSchedule)
            .collect(Collectors.toList());
    }
    
    private String formatSchedule(DoctorSchedule schedule) {
        return schedule.getDayOfWeek() + " " + schedule.getStartTime() + "-" + schedule.getEndTime() + " (" + schedule.getAppointmentDuration() + "m)";
    }

    public DoctorSchedule saveSchedule(DoctorSchedule schedule) {
        return scheduleRepository.save(schedule);
    }
    
    private DoctorDto toDto(Doctor doctor) {
        return new DoctorDto(
            doctor.getDoctorId().value(),
            doctor.getName(),
            doctor.getEmail(),
            doctor.getPhone(),
            doctor.getQualifications(),
            doctor.getSpecialties(),
            doctor.getAverageRating(),
            doctor.getTotalReviews()
        );
    }
}