package com.healthapp.appointment.domain.model.appointment;

public record AppointmentId(Long value) {
    public AppointmentId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Appointment ID must be positive");
        }
    }
}