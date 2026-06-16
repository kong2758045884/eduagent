package com.innovation.training.module.diagnosis.service;

import java.time.LocalDateTime;

public class StudentDiagnosisSummaryResponse {

    private String studentName;
    private String className;
    private String subject;
    private String latestTopic;
    private String latestRootCause;
    private String latestInterventions;
    private Long reportCount;
    private String riskLevel;
    private String latestImageUrl;
    private LocalDateTime latestAt;

    public String getLatestImageUrl() { return latestImageUrl; }
    public void setLatestImageUrl(String latestImageUrl) { this.latestImageUrl = latestImageUrl; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getLatestTopic() { return latestTopic; }
    public void setLatestTopic(String latestTopic) { this.latestTopic = latestTopic; }
    public String getLatestRootCause() { return latestRootCause; }
    public void setLatestRootCause(String latestRootCause) { this.latestRootCause = latestRootCause; }
    public String getLatestInterventions() { return latestInterventions; }
    public void setLatestInterventions(String latestInterventions) { this.latestInterventions = latestInterventions; }
    public Long getReportCount() { return reportCount; }
    public void setReportCount(Long reportCount) { this.reportCount = reportCount; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public LocalDateTime getLatestAt() { return latestAt; }
    public void setLatestAt(LocalDateTime latestAt) { this.latestAt = latestAt; }
}
