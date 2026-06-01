package com.innovation.training.module.casebase.dto;

import com.innovation.training.module.casebase.entity.LocalTeachingCase;

import java.time.LocalDateTime;

public class LocalTeachingCaseResponse {

    private Long id;
    private String title;
    private String subject;
    private String grade;
    private String knowledgePoint;
    private String localScenario;
    private String resourceType;
    private String content;
    private String tags;
    private String county;
    private String school;
    private Integer usageCount;
    private LocalDateTime updatedAt;

    public static LocalTeachingCaseResponse from(LocalTeachingCase item) {
        LocalTeachingCaseResponse response = new LocalTeachingCaseResponse();
        response.setId(item.getId());
        response.setTitle(item.getTitle());
        response.setSubject(item.getSubject());
        response.setGrade(item.getGrade());
        response.setKnowledgePoint(item.getKnowledgePoint());
        response.setLocalScenario(item.getLocalScenario());
        response.setResourceType(item.getResourceType());
        response.setContent(item.getContent());
        response.setTags(item.getTags());
        response.setCounty(item.getCounty());
        response.setSchool(item.getSchool());
        response.setUsageCount(item.getUsageCount());
        response.setUpdatedAt(item.getUpdatedAt());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getKnowledgePoint() { return knowledgePoint; }
    public void setKnowledgePoint(String knowledgePoint) { this.knowledgePoint = knowledgePoint; }
    public String getLocalScenario() { return localScenario; }
    public void setLocalScenario(String localScenario) { this.localScenario = localScenario; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
