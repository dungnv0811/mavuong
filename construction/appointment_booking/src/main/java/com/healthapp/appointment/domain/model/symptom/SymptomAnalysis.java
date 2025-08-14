package com.healthapp.appointment.domain.model.symptom;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "symptom_analyses")
public class SymptomAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    
    @Column(nullable = false, length = 1000)
    private String description;
    
    private String keywords;
    
    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt = LocalDateTime.now();
    
    @ElementCollection
    @CollectionTable(name = "recommendations", joinColumns = @JoinColumn(name = "analysis_id"))
    private List<String> suggestedSpecialties;
    
    protected SymptomAnalysis() {}
    
    public SymptomAnalysis(Long patientId, String description, List<String> suggestedSpecialties) {
        this.patientId = patientId;
        this.description = description;
        this.suggestedSpecialties = suggestedSpecialties;
    }
    
    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public String getDescription() { return description; }
    public List<String> getSuggestedSpecialties() { return suggestedSpecialties; }
    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
}