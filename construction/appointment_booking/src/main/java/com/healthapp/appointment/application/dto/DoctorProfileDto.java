package com.healthapp.appointment.application.dto;

public record DoctorProfileDto(
    String uuid,
    String firstName,
    String lastName,
    String professionalTitle,
    String primarySpecialty,
    Integer yearsExperience,
    String biography,
    String email,
    String phone,
    AddressDto address,
    String medicalLicenseNumber,
    String certifications,
    String memberships,
    String avatar
) {
    public record AddressDto(
        String street,
        String city,
        String state,
        String postalCode,
        String country
    ) {}
}
