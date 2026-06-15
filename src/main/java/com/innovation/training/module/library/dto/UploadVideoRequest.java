package com.innovation.training.module.library.dto;

public class UploadVideoRequest {

    private String title;
    private String summary;
    private String content;
    private String category;
    private String mediaUrl;
    private String coverUrl;
    private String duration;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}
