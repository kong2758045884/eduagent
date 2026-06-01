package com.innovation.training.module.research.dto;

import com.innovation.training.module.research.entity.ResearchTopic;

import java.time.LocalDateTime;

public class ResearchTopicResponse {

    private Long id;
    private String title;
    private String meta;
    private String extra;
    private String sources;
    private String applicationDraft;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ResearchTopicResponse from(ResearchTopic topic) {
        ResearchTopicResponse response = new ResearchTopicResponse();
        response.setId(topic.getId());
        response.setTitle(topic.getTitle());
        response.setMeta(topic.getMeta());
        response.setExtra(topic.getExtra());
        response.setSources(topic.getSources());
        response.setApplicationDraft(topic.getApplicationDraft());
        response.setCreatedAt(topic.getCreatedAt());
        response.setUpdatedAt(topic.getUpdatedAt());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMeta() { return meta; }
    public void setMeta(String meta) { this.meta = meta; }
    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }
    public String getSources() { return sources; }
    public void setSources(String sources) { this.sources = sources; }
    public String getApplicationDraft() { return applicationDraft; }
    public void setApplicationDraft(String applicationDraft) { this.applicationDraft = applicationDraft; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
