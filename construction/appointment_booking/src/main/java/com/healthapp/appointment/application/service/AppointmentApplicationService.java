package com.healthapp.appointment.application.service;

import com.healthapp.appointment.application.dto.AppointmentDto;
import com.healthapp.appointment.domain.model.appointment.Appointment;
import com.healthapp.appointment.domain.model.appointment.AppointmentId;
import com.healthapp.appointment.domain.repository.AppointmentRepository;
import com.healthapp.appointment.domain.service.AppointmentBookingService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentApplicationService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentBookingService bookingService;
    
    public AppointmentApplicationService(AppointmentRepository appointmentRepository, 
                                       AppointmentBookingService bookingService) {
        this.appointmentRepository = appointmentRepository;
        this.bookingService = bookingService;
    }
    
    public AppointmentDto bookAppointment(Long patientId, Long doctorId, LocalDateTime dateTime, String notes) {
        Appointment appointment = bookingService.bookAppointment(patientId, doctorId, dateTime, notes);
        return toDto(appointment);
    }
    
    public List<AppointmentDto> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
            .map(this::toDto)
            .toList();
    }
    
    public List<AppointmentDto> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
            .map(this::toDto)
            .toList();
    }
    
    public AppointmentDto confirmAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(new AppointmentId(appointmentId))
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.confirm();
        return toDto(appointmentRepository.save(appointment));
    }
    
    private AppointmentDto toDto(Appointment appointment) {
        return new AppointmentDto(
            appointment.getAppointmentId().value(),
            appointment.getPatientId(),
            appointment.getDoctorId(),
            appointment.getAppointmentDateTime(),
            appointment.getDurationMinutes(),
            appointment.getNotes(),
            appointment.getStatus()
        );
    }
}