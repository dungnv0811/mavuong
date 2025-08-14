package com.healthapp.appointment.domain.model.doctor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String phone;
    private String qualifications;
    
    @Column(name = "average_rating")
    private Double averageRating = 0.0;
    
    @Column(name = "total_reviews")
    private Integer totalReviews = 0;
    
    @ElementCollection
    @CollectionTable(name = "doctor_specialties", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "specialty_type")
    private List<String> specialties;
    
    protected Doctor() {}
    
    public Doctor(String name, String email, String phone, String qualifications, List<String> specialties) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.qualifications = qualifications;
        this.specialties = specialties;
    }
    
    public DoctorId getDoctorId() {
        return new DoctorId(id);
    }
    
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getQualifications() { return qualifications; }
    public List<String> getSpecialties() { return specialties; }
    public Double getAverageRating() { return averageRating; }
    public Integer getTotalReviews() { return totalReviews; }
    
    public void updateRating(double newRating) {
        double totalScore = averageRating * totalReviews + newRating;
        totalReviews++;
        averageRating = totalScore / totalReviews;
    }
}