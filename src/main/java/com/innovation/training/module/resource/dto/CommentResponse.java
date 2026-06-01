package com.innovation.training.module.resource.dto;

import com.innovation.training.module.resource.comment.ResourceComment;

import java.time.LocalDateTime;

public class CommentResponse {

    private Long id;
    private Long resourceId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponse from(ResourceComment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setResourceId(comment.getResourceId());
        response.setUserId(comment.getUserId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getResourceId() { return resourceId; }
    public void setResourceId(Long resourceId) { this.resourceId = resourceId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
