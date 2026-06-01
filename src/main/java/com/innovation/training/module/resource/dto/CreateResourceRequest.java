package com.innovation.training.module.resource.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateResourceRequest {

    @NotBlank(message = "资源标题不能为空")
    private String title;

    private String summary;

    private String content;

    private String resourceType = "lesson";

    private String subject = "数学";

    private String grade;

    private String county;

    private String school;

    private String coverUrl;

    private String mediaUrl;

    private String duration;

    private String uploader;

    private String tags;

    private String sourceType;

    private Long sourceId;

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

    public String getSourceType() { return sourceType; }

    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public Long getSourceId() { return sourceId; }

    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
}
