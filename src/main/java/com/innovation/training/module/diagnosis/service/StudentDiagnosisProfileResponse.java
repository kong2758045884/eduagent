package com.innovation.training.module.diagnosis.service;

import com.innovation.training.module.diagnosis.dto.DiagnosisResponse;

import java.util.List;
import java.util.Map;

public class StudentDiagnosisProfileResponse {

    private StudentDiagnosisSummaryResponse summary;
    private Map<String, Long> topicDistribution;
    private List<DiagnosisResponse> reports;

    public StudentDiagnosisSummaryResponse getSummary() { return summary; }
    public void setSummary(StudentDiagnosisSummaryResponse summary) { this.summary = summary; }
    public Map<String, Long> getTopicDistribution() { return topicDistribution; }
    public void setTopicDistribution(Map<String, Long> topicDistribution) { this.topicDistribution = topicDistribution; }
    public List<DiagnosisResponse> getReports() { return reports; }
    public void setReports(List<DiagnosisResponse> reports) { this.reports = reports; }
}
