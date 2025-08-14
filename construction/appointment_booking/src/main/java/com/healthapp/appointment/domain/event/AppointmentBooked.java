package com.healthapp.appointment.domain.event;

import java.time.LocalDateTime;

public record AppointmentBooked(
    Long appointmentId,
    Long patientId,
    Long doctorId,
    LocalDateTime appointmentDateTime
) {}


