package com.healthapp.appointment.application.service;

import com.healthapp.appointment.application.dto.AppointmentDto;
import com.healthapp.appointment.domain.event.AppointmentBooked;
import com.healthapp.appointment.domain.event.AppointmentConfirmed;
import com.healthapp.appointment.domain.model.appointment.Appointment;
import com.healthapp.appointment.domain.model.appointment.AppointmentId;
import com.healthapp.appointment.domain.repository.AppointmentRepository;
import com.healthapp.appointment.domain.service.AppointmentBookingService;
import com.healthapp.appointment.infrastructure.event.EventPublisher;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentApplicationService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentBookingService bookingService;
    private final EventPublisher eventPublisher;
    
    public AppointmentApplicationService(AppointmentRepository appointmentRepository, 
                                       AppointmentBookingService bookingService,
                                       EventPublisher eventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.bookingService = bookingService;
        this.eventPublisher = eventPublisher;
    }
    
    public AppointmentDto bookAppointment(Long patientId, Long doctorId, LocalDateTime dateTime, String notes) {
        Appointment appointment = bookingService.bookAppointment(patientId, doctorId, dateTime, notes);
        eventPublisher.publish(new AppointmentBooked(
            appointment.getAppointmentId().value(),
            appointment.getPatientId(),
            appointment.getDoctorId(),
            appointment.getAppointmentDateTime()
        ));
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
    
    public AppointmentDto getAppointmentById(Long id) {
        return appointmentRepository.findById(new AppointmentId(id))
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }
    
    public AppointmentDto confirmAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(new AppointmentId(appointmentId))
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.confirm();
        Appointment saved = appointmentRepository.save(appointment);
        eventPublisher.publish(new AppointmentConfirmed(saved.getAppointmentId().value(), saved.getDoctorId()));
        return toDto(saved);
    }
 
    public AppointmentDto cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(new AppointmentId(appointmentId))
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.cancel();
        return toDto(appointmentRepository.save(appointment));
    }
 
    public AppointmentDto rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime) {
        Appointment appointment = appointmentRepository.findById(new AppointmentId(appointmentId))
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.reschedule(newDateTime);
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