package com.healthapp.appointment.application.dto;

import java.util.List;

public record DoctorScheduleDto(
    String doctorID,
    String date,
    List<TimeSlotDto> timeSlots
) {}
