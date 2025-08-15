package com.healthapp.appointment.application.dto;

import com.healthapp.appointment.domain.model.user.UserRole;
import java.time.LocalDate;

public record UserDto(
    String uuid,
    String username,
    String firstName,
    String lastName,
    String email,
    String phone,
    LocalDate dob,
    String address,
    UserRole userRole
) {}
