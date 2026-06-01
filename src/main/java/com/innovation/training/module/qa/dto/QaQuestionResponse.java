package com.innovation.training.module.qa.dto;

import com.innovation.training.module.qa.entity.QaQuestion;

import java.time.LocalDateTime;
import java.util.List;

public class QaQuestionResponse {

    private Long id;
    private Long userId;
    private String topic;
    private String content;
    private String status;
    private Long mentorUserId;
    private String sourceType;
    private Long sourceId;
    private LocalDateTime createdAt;
    private List<QaReplyResponse> replies;

    public static QaQuestionResponse from(QaQuestion question, List<QaReplyResponse> replies) {
        QaQuestionResponse response = new QaQuestionResponse();
        response.setId(question.getId());
        response.setUserId(question.getUserId());
        response.setTopic(question.getTopic());
        response.setContent(question.getContent());
        response.setStatus(question.getStatus());
        response.setMentorUserId(question.getMentorUserId());
        response.setSourceType(question.getSourceType());
        response.setSourceId(question.getSourceId());
        response.setCreatedAt(question.getCreatedAt());
        response.setReplies(replies);
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getMentorUserId() { return mentorUserId; }
    public void setMentorUserId(Long mentorUserId) { this.mentorUserId = mentorUserId; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<QaReplyResponse> getReplies() { return replies; }
    public void setReplies(List<QaReplyResponse> replies) { this.replies = replies; }
}
