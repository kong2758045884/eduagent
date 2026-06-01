package com.innovation.training.module.notification.dto;

import com.innovation.training.module.notification.entity.AppNotification;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;
    private String title;
    private String content;
    private String type;
    private String sourceType;
    private Long sourceId;
    private Boolean read;
    private LocalDateTime createdAt;

    public static NotificationResponse from(AppNotification item) {
        NotificationResponse response = new NotificationResponse();
        response.setId(item.getId());
        response.setTitle(item.getTitle());
        response.setContent(item.getContent());
        response.setType(item.getType());
        response.setSourceType(item.getSourceType());
        response.setSourceId(item.getSourceId());
        response.setRead(item.getReadStatus() != null && item.getReadStatus() == 1);
        response.setCreatedAt(item.getCreatedAt());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public Boolean getRead() { return read; }
    public void setRead(Boolean read) { this.read = read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
