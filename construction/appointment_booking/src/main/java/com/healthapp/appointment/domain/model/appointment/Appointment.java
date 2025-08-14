package com.healthapp.appointment.domain.model.appointment;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
    
    @Column(name = "appointment_date_time", nullable = false)
    private LocalDateTime appointmentDateTime;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes = 30;
    
    private String notes;
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;
    
    @Column(name = "time_zone")
    private String timeZone = "UTC";
    
    protected Appointment() {}
    
    public Appointment(Long patientId, Long doctorId, LocalDateTime appointmentDateTime, String notes) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.notes = notes;
    }
    
    public AppointmentId getAppointmentId() {
        return new AppointmentId(id);
    }
    
    public Long getPatientId() { return patientId; }
    public Long getDoctorId() { return doctorId; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public String getNotes() { return notes; }
    public AppointmentStatus getStatus() { return status; }
    public Integer getDurationMinutes() { return durationMinutes; }
    
    public void confirm() {
        if (status != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Only pending appointments can be confirmed");
        }
        status = AppointmentStatus.CONFIRMED;
    }
    
    public void cancel() {
        if (status == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Completed appointments cannot be cancelled");
        }
        status = AppointmentStatus.CANCELLED;
    }
    
    public void reschedule(LocalDateTime newDateTime) {
        if (status != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed appointments can be rescheduled");
        }
        this.appointmentDateTime = newDateTime;
    }
}