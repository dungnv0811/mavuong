package com.healthapp.appointment.domain.event;

public record AppointmentConfirmed(
    Long appointmentId,
    Long doctorId
) {}


