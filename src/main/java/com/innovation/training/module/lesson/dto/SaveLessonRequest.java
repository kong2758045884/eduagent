package com.innovation.training.module.lesson.dto;

import jakarta.validation.constraints.NotBlank;

public class SaveLessonRequest {

    @NotBlank(message = "教案标题不能为空")
    private String title;

    private String summary;

    private String requirement;

    @NotBlank(message = "教案内容不能为空")
    private String content;

    private String contentJson;

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
}
