package com.healthapp.appointment.infrastructure.repository;

import com.healthapp.appointment.domain.model.symptom.SymptomAnalysis;
import com.healthapp.appointment.domain.repository.SymptomAnalysisRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSymptomAnalysisRepository extends JpaRepository<SymptomAnalysis, Long>, SymptomAnalysisRepository {
}


