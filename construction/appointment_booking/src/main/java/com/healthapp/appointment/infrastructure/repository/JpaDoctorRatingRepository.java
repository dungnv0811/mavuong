package com.healthapp.appointment.infrastructure.repository;

import com.healthapp.appointment.domain.model.doctor.DoctorRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaDoctorRatingRepository extends JpaRepository<DoctorRating, Long> {
    List<DoctorRating> findByDoctorId(Long doctorId);
}


