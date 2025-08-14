package com.healthapp.appointment.application.dto;

import com.healthapp.appointment.domain.model.appointment.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentDto(
    Long id,
    Long patientId,
    Long doctorId,
    LocalDateTime appointmentDateTime,
    Integer durationMinutes,
    String notes,
    AppointmentStatus status
) {}