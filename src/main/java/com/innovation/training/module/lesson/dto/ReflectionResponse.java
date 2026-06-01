package com.innovation.training.module.lesson.dto;

import com.innovation.training.module.lesson.entity.LessonReflection;

import java.time.LocalDateTime;

public class ReflectionResponse {

    private Long id;
    private Long lessonId;
    private String text;
    private String aiSummary;
    private Boolean shared;
    private LocalDateTime createdAt;

    public static ReflectionResponse from(LessonReflection reflection) {
        ReflectionResponse response = new ReflectionResponse();
        response.setId(reflection.getId());
        response.setLessonId(reflection.getLessonId());
        response.setText(reflection.getText());
        response.setAiSummary(reflection.getAiSummary());
        response.setShared(reflection.getShared() != null && reflection.getShared() == 1);
        response.setCreatedAt(reflection.getCreatedAt());
        return response;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getLessonId() { return lessonId; }

    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public String getAiSummary() { return aiSummary; }

    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }

    public Boolean getShared() { return shared; }

    public void setShared(Boolean shared) { this.shared = shared; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
