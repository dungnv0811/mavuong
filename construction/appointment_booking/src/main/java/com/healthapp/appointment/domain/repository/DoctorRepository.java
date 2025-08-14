package com.healthapp.appointment.domain.repository;

import com.healthapp.appointment.domain.model.doctor.Doctor;
import com.healthapp.appointment.domain.model.doctor.DoctorId;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository {
    Optional<Doctor> findById(DoctorId doctorId);
    List<Doctor> findBySpecialty(String specialty);
    List<Doctor> findAll();
    Doctor save(Doctor doctor);
    // schedule and ratings will be handled via separate repositories/services
}