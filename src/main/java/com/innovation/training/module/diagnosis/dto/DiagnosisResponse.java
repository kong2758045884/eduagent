package com.innovation.training.module.diagnosis.dto;

import com.innovation.training.module.diagnosis.entity.DiagnosisReport;

import java.time.LocalDateTime;

public class DiagnosisResponse {

    private Long id;
    private String studentName;
    private String className;
    private String subject;
    private String topic;
    private String questionText;
    private String answerText;
    private String imageUrl;
    private String reportJson;
    private String rootCause;
    private String interventions;
    private LocalDateTime createdAt;

    public static DiagnosisResponse from(DiagnosisReport report) {
        DiagnosisResponse response = new DiagnosisResponse();
        response.setId(report.getId());
        response.setStudentName(report.getStudentName());
        response.setClassName(report.getClassName());
        response.setSubject(report.getSubject());
        response.setTopic(report.getTopic());
        response.setQuestionText(report.getQuestionText());
        response.setAnswerText(report.getAnswerText());
        response.setImageUrl(report.getImageUrl());
        response.setReportJson(report.getReportJson());
        response.setRootCause(report.getRootCause());
        response.setInterventions(report.getInterventions());
        response.setCreatedAt(report.getCreatedAt());
        return response;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getStudentName() { return studentName; }

    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getClassName() { return className; }

    public void setClassName(String className) { this.className = className; }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getTopic() { return topic; }

    public void setTopic(String topic) { this.topic = topic; }

    public String getQuestionText() { return questionText; }

    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getAnswerText() { return answerText; }

    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getReportJson() { return reportJson; }

    public void setReportJson(String reportJson) { this.reportJson = reportJson; }

    public String getRootCause() { return rootCause; }

    public void setRootCause(String rootCause) { this.rootCause = rootCause; }

    public String getInterventions() { return interventions; }

    public void setInterventions(String interventions) { this.interventions = interventions; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
