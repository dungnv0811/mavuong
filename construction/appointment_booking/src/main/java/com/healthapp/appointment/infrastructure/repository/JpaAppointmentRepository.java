package com.healthapp.appointment.infrastructure.repository;

import com.healthapp.appointment.domain.model.appointment.Appointment;
import com.healthapp.appointment.domain.model.appointment.AppointmentId;
import com.healthapp.appointment.domain.repository.AppointmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaAppointmentRepository extends JpaRepository<Appointment, Long>, AppointmentRepository {
    
    @Override
    default Optional<Appointment> findById(AppointmentId appointmentId) {
        return findById(appointmentId.value());
    }
    
    @Override
    List<Appointment> findByPatientId(Long patientId);
    
    @Override
    List<Appointment> findByDoctorId(Long doctorId);
    
    @Override
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDateTime = :dateTime")
    List<Appointment> findByDoctorIdAndDateTime(Long doctorId, LocalDateTime dateTime);
}