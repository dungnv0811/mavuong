package com.healthapp.appointment.domain.service;

import com.healthapp.appointment.domain.model.appointment.Appointment;
import com.healthapp.appointment.domain.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentBookingService {
    private final AppointmentRepository appointmentRepository;
    
    public AppointmentBookingService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
    
    public Appointment bookAppointment(Long patientId, Long doctorId, LocalDateTime dateTime, String notes) {
        // Check for conflicts
        List<Appointment> conflicts = appointmentRepository.findByDoctorIdAndDateTime(doctorId, dateTime);
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Time slot already booked");
        }
        
        Appointment appointment = new Appointment(patientId, doctorId, dateTime, notes);
        return appointmentRepository.save(appointment);
    }
    
    public boolean isTimeSlotAvailable(Long doctorId, LocalDateTime dateTime) {
        return appointmentRepository.findByDoctorIdAndDateTime(doctorId, dateTime).isEmpty();
    }
}