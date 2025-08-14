package com.healthapp.appointment.domain.model.symptom;

import jakarta.persistence.*;

@Entity
@Table(name = "recommendations")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "analysis_id", nullable = false)
    private Long analysisId;

    @Column(name = "specialty", nullable = false)
    private String specialty;

    @Column(name = "confidence")
    private Double confidence;

    @Column(name = "suggested_doctor_ids")
    private String suggestedDoctorIds; // comma-separated IDs for demo

    protected Recommendation() {}

    public Recommendation(Long analysisId, String specialty, Double confidence, String suggestedDoctorIds) {
        this.analysisId = analysisId;
        this.specialty = specialty;
        this.confidence = confidence;
        this.suggestedDoctorIds = suggestedDoctorIds;
    }

    public Long getId() { return id; }
    public Long getAnalysisId() { return analysisId; }
    public String getSpecialty() { return specialty; }
    public Double getConfidence() { return confidence; }
    public String getSuggestedDoctorIds() { return suggestedDoctorIds; }
}


