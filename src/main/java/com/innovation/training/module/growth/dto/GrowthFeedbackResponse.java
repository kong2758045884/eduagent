package com.innovation.training.module.growth.dto;

import com.innovation.training.module.growth.entity.GrowthFeedback;

import java.time.LocalDateTime;

public class GrowthFeedbackResponse {

    private Long id;
    private String feedbackType;
    private String source;
    private String title;
    private String content;
    private String sourceType;
    private Long sourceId;
    private LocalDateTime createdAt;

    public static GrowthFeedbackResponse from(GrowthFeedback feedback) {
        GrowthFeedbackResponse response = new GrowthFeedbackResponse();
        response.setId(feedback.getId());
        response.setFeedbackType(feedback.getFeedbackType());
        response.setSource(feedback.getSource());
        response.setTitle(feedback.getTitle());
        response.setContent(feedback.getContent());
        response.setSourceType(feedback.getSourceType());
        response.setSourceId(feedback.getSourceId());
        response.setCreatedAt(feedback.getCreatedAt());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFeedbackType() { return feedbackType; }
    public void setFeedbackType(String feedbackType) { this.feedbackType = feedbackType; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
