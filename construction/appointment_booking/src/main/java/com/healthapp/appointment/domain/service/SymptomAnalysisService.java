package com.healthapp.appointment.domain.service;

import com.healthapp.appointment.domain.model.symptom.SymptomAnalysis;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class SymptomAnalysisService {
    private static final Map<String, List<String>> SYMPTOM_KEYWORDS = Map.of(
        "cardiology", Arrays.asList("chest pain", "heart", "palpitation", "shortness of breath"),
        "dermatology", Arrays.asList("skin", "rash", "acne", "eczema", "itching"),
        "orthopedics", Arrays.asList("bone", "joint", "back pain", "fracture", "muscle"),
        "neurology", Arrays.asList("headache", "migraine", "seizure", "memory", "dizziness"),
        "general", Arrays.asList("fever", "cold", "flu", "cough", "fatigue")
    );
    
    public SymptomAnalysis analyzeSymptoms(Long patientId, String description) {
        String lowerDescription = description.toLowerCase();
        List<String> suggestedSpecialties = SYMPTOM_KEYWORDS.entrySet().stream()
            .filter(entry -> entry.getValue().stream()
                .anyMatch(lowerDescription::contains))
            .map(Map.Entry::getKey)
            .toList();
        
        if (suggestedSpecialties.isEmpty()) {
            suggestedSpecialties = List.of("general");
        }
        
        return new SymptomAnalysis(patientId, description, suggestedSpecialties);
    }
}