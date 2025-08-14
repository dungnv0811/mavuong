package com.healthapp.appointment.domain.repository;

import com.healthapp.appointment.domain.model.appointment.Appointment;
import com.healthapp.appointment.domain.model.appointment.AppointmentId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    Optional<Appointment> findById(AppointmentId appointmentId);
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByDoctorIdAndDateTime(Long doctorId, LocalDateTime dateTime);
    Appointment save(Appointment appointment);
}