package com.healthapp.appointment.infrastructure.repository;

import com.healthapp.appointment.domain.model.doctor.Doctor;
import com.healthapp.appointment.domain.model.doctor.DoctorId;
import com.healthapp.appointment.domain.repository.DoctorRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaDoctorRepository extends JpaRepository<Doctor, Long>, DoctorRepository {
    
    @Override
    default Optional<Doctor> findById(DoctorId doctorId) {
        return findById(doctorId.value());
    }
    
    @Override
    @Query("SELECT d FROM Doctor d JOIN d.specialties s WHERE s = :specialty")
    List<Doctor> findBySpecialty(String specialty);
}