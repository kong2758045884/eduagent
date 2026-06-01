package com.innovation.training.module.growth.dto;

import com.innovation.training.module.growth.entity.GrowthEvent;

import java.time.LocalDateTime;

public class GrowthEventResponse {

    private Long id;
    private String eventType;
    private String title;
    private String content;
    private String sourceType;
    private Long sourceId;
    private LocalDateTime eventTime;

    public static GrowthEventResponse from(GrowthEvent event) {
        GrowthEventResponse response = new GrowthEventResponse();
        response.setId(event.getId());
        response.setEventType(event.getEventType());
        response.setTitle(event.getTitle());
        response.setContent(event.getContent());
        response.setSourceType(event.getSourceType());
        response.setSourceId(event.getSourceId());
        response.setEventTime(event.getEventTime());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
