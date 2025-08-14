package com.healthapp.appointment.domain.event;

import java.util.List;

public record SymptomAnalyzed(
    Long analysisId,
    Long patientId,
    List<String> suggestedSpecialties
) {}


