package com.healthapp.appointment.domain.model.doctor;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor_ratings")
public class DoctorRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    private int stars; // 1-5

    @Column(length = 1000)
    private String feedback;

    protected DoctorRating() {}

    public DoctorRating(Long doctorId, Long patientId, int stars, String feedback) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.stars = stars;
        this.feedback = feedback;
    }

    public Long getDoctorId() { return doctorId; }
    public Long getPatientId() { return patientId; }
    public int getStars() { return stars; }
    public String getFeedback() { return feedback; }
}


