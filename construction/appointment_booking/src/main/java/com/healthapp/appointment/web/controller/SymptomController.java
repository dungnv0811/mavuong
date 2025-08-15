package com.healthapp.appointment.web.controller;

import com.healthapp.appointment.application.service.SymptomApplicationService;
import com.healthapp.appointment.domain.model.symptom.SymptomAnalysis;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/symptoms")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Symptoms", description = "Symptom analysis operations for patients")
@SecurityRequirement(name = "bearer-key")
public class SymptomController {
    private final SymptomApplicationService symptomService;
    
    public SymptomController(SymptomApplicationService symptomService) {
        this.symptomService = symptomService;
    }
    
    @PostMapping("/analyze")
    @Operation(summary = "Analyze symptoms", description = "Analyze patient symptoms and suggest medical specialties (patients only)")
    public SymptomAnalysisResponse analyzeSymptoms(@RequestBody SymptomAnalysisRequest request) {
        SymptomAnalysis analysis = symptomService.analyze(request.patientId(), request.description());
        return new SymptomAnalysisResponse(analysis.getSuggestedSpecialties());
    }
    
    public record SymptomAnalysisRequest(Long patientId, String description) {}
    public record SymptomAnalysisResponse(List<String> suggestedSpecialties) {}
}