package com.innovation.training.module.casebase.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateLocalTeachingCaseRequest {

    @NotBlank(message = "案例标题不能为空")
    private String title;
    private String subject = "数学";
    private String grade;
    private String knowledgePoint;
    private String localScenario;
    private String resourceType = "scenario";
    private String content;
    private String tags;
    private String county;
    private String school;

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
}
