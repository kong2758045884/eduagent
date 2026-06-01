package com.innovation.training.module.lesson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateReflectionRequest {

    @NotNull(message = "教案 ID 不能为空")
    private Long lessonId;

    @NotBlank(message = "反思内容不能为空")
    private String text;

    private Boolean shared;

    public Long getLessonId() { return lessonId; }

    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public Boolean getShared() { return shared; }

    public void setShared(Boolean shared) { this.shared = shared; }
}
