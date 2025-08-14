package com.healthapp.appointment.domain.model.doctor;

import java.util.List;

public record Specialty(
    String type,
    List<String> keywords,
    String description
) {
    public Specialty {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialty type cannot be empty");
        }
    }
}