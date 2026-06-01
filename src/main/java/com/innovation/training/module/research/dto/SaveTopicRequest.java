package com.innovation.training.module.research.dto;

import jakarta.validation.constraints.NotBlank;

public class SaveTopicRequest {

    @NotBlank(message = "课题名称不能为空")
    private String title;

    private String meta;

    private String extra;

    private String sources;

    private String applicationDraft;

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
}
