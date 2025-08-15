package com.healthapp.appointment.application.dto;

public record DoctorListingDto(
    String doctorID,
    String name,
    String specialty,
    Double rating,
    Integer reviews,
    String nextAvailable,
    String avatar
) {}
