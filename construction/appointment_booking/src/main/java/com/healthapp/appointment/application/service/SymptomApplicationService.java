package com.healthapp.appointment.application.service;

import com.healthapp.appointment.domain.event.SymptomAnalyzed;
import com.healthapp.appointment.domain.model.symptom.SymptomAnalysis;
import com.healthapp.appointment.domain.repository.SymptomAnalysisRepository;
import com.healthapp.appointment.domain.service.SymptomAnalysisService;
import com.healthapp.appointment.infrastructure.event.EventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SymptomApplicationService {
    private final SymptomAnalysisService symptomAnalysisService;
    private final SymptomAnalysisRepository symptomAnalysisRepository;
    private final EventPublisher eventPublisher;

    public SymptomApplicationService(SymptomAnalysisService symptomAnalysisService,
                                     SymptomAnalysisRepository symptomAnalysisRepository,
                                     EventPublisher eventPublisher) {
        this.symptomAnalysisService = symptomAnalysisService;
        this.symptomAnalysisRepository = symptomAnalysisRepository;
        this.eventPublisher = eventPublisher;
    }

    public SymptomAnalysis analyze(Long patientId, String description) {
        SymptomAnalysis analysis = symptomAnalysisService.analyzeSymptoms(patientId, description);
        SymptomAnalysis saved = symptomAnalysisRepository.save(analysis);
        eventPublisher.publish(new SymptomAnalyzed(saved.getId(), patientId, saved.getSuggestedSpecialties()));
        return saved;
    }
}


