package com.healthapp.appointment.application.service;

import com.healthapp.appointment.application.dto.DoctorDto;
import com.healthapp.appointment.domain.model.doctor.Doctor;
import com.healthapp.appointment.domain.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DoctorApplicationService {
    private final DoctorRepository doctorRepository;
    
    public DoctorApplicationService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
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