package com.innovation.training.module.experience.dto;

import jakarta.validation.constraints.NotBlank;

public class ShareExperienceRequest {

    private Long lessonId;
    private Long reflectionId;

    @NotBlank(message = "资源标题不能为空")
    private String title;

    private String summary;
    private String category = "experience";
    private Boolean authorized = true;

    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }
    public Long getReflectionId() { return reflectionId; }
    public void setReflectionId(Long reflectionId) { this.reflectionId = reflectionId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Boolean getAuthorized() { return authorized; }
    public void setAuthorized(Boolean authorized) { this.authorized = authorized; }
}
