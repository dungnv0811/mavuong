package com.healthapp.appointment.domain.model.doctor;

public record DoctorId(Long value) {
    public DoctorId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Doctor ID must be positive");
        }
    }
}