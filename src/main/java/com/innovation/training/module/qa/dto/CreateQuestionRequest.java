package com.innovation.training.module.qa.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateQuestionRequest {

    private String topic;

    @NotBlank(message = "问题内容不能为空")
    private String content;

    private Long mentorUserId;
    private String sourceType;
    private Long sourceId;

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getMentorUserId() { return mentorUserId; }
    public void setMentorUserId(Long mentorUserId) { this.mentorUserId = mentorUserId; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
}
