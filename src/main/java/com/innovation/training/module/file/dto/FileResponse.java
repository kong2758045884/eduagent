package com.innovation.training.module.file.dto;

import com.innovation.training.module.file.entity.AppFile;

import java.time.LocalDateTime;

public class FileResponse {

    private Long id;
    private String originalName;
    private String contentType;
    private String publicUrl;
    private Long size;
    private String bizType;
    private Long bizId;
    private LocalDateTime createdAt;

    public static FileResponse from(AppFile file) {
        FileResponse response = new FileResponse();
        response.setId(file.getId());
        response.setOriginalName(file.getOriginalName());
        response.setContentType(file.getContentType());
        response.setPublicUrl(file.getPublicUrl());
        response.setSize(file.getSize());
        response.setBizType(file.getBizType());
        response.setBizId(file.getBizId());
        response.setCreatedAt(file.getCreatedAt());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getPublicUrl() { return publicUrl; }
    public void setPublicUrl(String publicUrl) { this.publicUrl = publicUrl; }
    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }
    public String getBizType() { return bizType; }
    public void setBizType(String bizType) { this.bizType = bizType; }
    public Long getBizId() { return bizId; }
    public void setBizId(Long bizId) { this.bizId = bizId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
