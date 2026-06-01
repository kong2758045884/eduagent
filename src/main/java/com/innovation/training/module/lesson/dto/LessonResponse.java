package com.innovation.training.module.lesson.dto;

import com.innovation.training.module.lesson.entity.LessonDraft;

import java.time.LocalDateTime;

public class LessonResponse {

    private Long id;
    private String title;
    private String summary;
    private String requirement;
    private String content;
    private String contentJson;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LessonResponse from(LessonDraft lesson) {
        LessonResponse response = new LessonResponse();
        response.setId(lesson.getId());
        response.setTitle(lesson.getTitle());
        response.setSummary(lesson.getSummary());
        response.setRequirement(lesson.getRequirement());
        response.setContent(lesson.getContent());
        response.setContentJson(lesson.getContentJson());
        response.setCreatedAt(lesson.getCreatedAt());
        response.setUpdatedAt(lesson.getUpdatedAt());
        return response;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }

    public void setSummary(String summary) { this.summary = summary; }

    public String getRequirement() { return requirement; }

    public void setRequirement(String requirement) { this.requirement = requirement; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getContentJson() { return contentJson; }

    public void setContentJson(String contentJson) { this.contentJson = contentJson; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
