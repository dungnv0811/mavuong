package com.healthapp.appointment.application.dto;

public record TimeSlotDto(
    String time,
    boolean isBooked,
    boolean isDisabled
) {}
