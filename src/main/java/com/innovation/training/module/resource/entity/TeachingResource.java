package com.innovation.training.module.resource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("teaching_resource")
public class TeachingResource {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    private String title;

    private String summary;

    private String content;

    @TableField("resource_type")
    private String resourceType;

    private String subject;

    private String grade;

    private String county;

    private String school;

    @TableField("cover_url")
    private String coverUrl;

    @TableField("media_url")
    private String mediaUrl;

    private String duration;

    private String uploader;

    private String tags;

    @TableField("view_count")
    private Integer viewCount;

    @TableField("favorite_count")
    private Integer favoriteCount;

    @TableField("source_type")
    private String sourceType;

    @TableField("source_id")
    private Long sourceId;

    @TableField("audit_status")
    private String auditStatus;

    private Integer likes;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }

    public void setSummary(String summary) { this.summary = summary; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getResourceType() { return resourceType; }

    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getGrade() { return grade; }

    public void setGrade(String grade) { this.grade = grade; }

    public String getCounty() { return county; }

    public void setCounty(String county) { this.county = county; }

    public String getSchool() { return school; }

    public void setSchool(String school) { this.school = school; }

    public String getCoverUrl() { return coverUrl; }

    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public String getMediaUrl() { return mediaUrl; }

    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public String getDuration() { return duration; }

    public void setDuration(String duration) { this.duration = duration; }

    public String getUploader() { return uploader; }

    public void setUploader(String uploader) { this.uploader = uploader; }

    public String getTags() { return tags; }

    public void setTags(String tags) { this.tags = tags; }

    public Integer getViewCount() { return viewCount; }

    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Integer getFavoriteCount() { return favoriteCount; }

    public void setFavoriteCount(Integer favoriteCount) { this.favoriteCount = favoriteCount; }

    public String getSourceType() { return sourceType; }

    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public Long getSourceId() { return sourceId; }

    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }

    public String getAuditStatus() { return auditStatus; }

    public void setAuditStatus(String auditStatus) { this.auditStatus = auditStatus; }

    public Integer getLikes() { return likes; }

    public void setLikes(Integer likes) { this.likes = likes; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
