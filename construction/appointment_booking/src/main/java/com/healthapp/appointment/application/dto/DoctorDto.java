package com.healthapp.appointment.application.dto;

import java.util.List;

public record DoctorDto(
    Long id,
    String name,
    String email,
    String phone,
    String qualifications,
    List<String> specialties,
    Double averageRating,
    Integer totalReviews
) {}