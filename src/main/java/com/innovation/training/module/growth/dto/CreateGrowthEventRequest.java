package com.innovation.training.module.growth.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class CreateGrowthEventRequest {

    private String eventType = "manual";

    @NotBlank(message = "成长事件标题不能为空")
    private String title;

    private String content;

    private String sourceType;

    private Long sourceId;

    private LocalDateTime eventTime;

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public LocalDateTime getEventTime() { return eventTime; }
    public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }
}
