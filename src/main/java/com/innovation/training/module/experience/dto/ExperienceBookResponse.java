package com.innovation.training.module.experience.dto;

import java.util.List;

public class ExperienceBookResponse {

    private Long lessonCount;
    private Long reflectionCount;
    private List<ExperienceItemResponse> items;

    public ExperienceBookResponse() {
    }

    public ExperienceBookResponse(Long lessonCount, Long reflectionCount, List<ExperienceItemResponse> items) {
        this.lessonCount = lessonCount;
        this.reflectionCount = reflectionCount;
        this.items = items;
    }

    public Long getLessonCount() { return lessonCount; }
    public void setLessonCount(Long lessonCount) { this.lessonCount = lessonCount; }
    public Long getReflectionCount() { return reflectionCount; }
    public void setReflectionCount(Long reflectionCount) { this.reflectionCount = reflectionCount; }
    public List<ExperienceItemResponse> getItems() { return items; }
    public void setItems(List<ExperienceItemResponse> items) { this.items = items; }
}
