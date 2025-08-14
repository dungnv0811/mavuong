package com.healthapp.appointment.application.dto;

public record RatingDto(Long doctorId, Long patientId, int stars, String feedback) {}


