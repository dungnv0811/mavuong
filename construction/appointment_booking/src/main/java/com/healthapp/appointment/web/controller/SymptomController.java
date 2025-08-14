package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.domain.model.symptom.SymptomAnalysis;
import com.healthapp.appointment.domain.service.SymptomAnalysisService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/symptoms")
@CrossOrigin(origins = "http://localhost:4200")
public class SymptomController {
    private final SymptomAnalysisService symptomService;
    
    public SymptomController(SymptomAnalysisService symptomService) {
        this.symptomService = symptomService;
    }
    
    @PostMapping("/analyze")
    public SymptomAnalysisResponse analyzeSymptoms(@RequestBody SymptomAnalysisRequest request) {
        SymptomAnalysis analysis = symptomService.analyzeSymptoms(request.patientId(), request.description());
        return new SymptomAnalysisResponse(analysis.getSuggestedSpecialties());
    }
    
    public record SymptomAnalysisRequest(Long patientId, String description) {}
    public record SymptomAnalysisResponse(List<String> suggestedSpecialties) {}
}